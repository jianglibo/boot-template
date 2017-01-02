package hello.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hello.config.userdetail.BootUserDetailManager;
import hello.domain.LoginAttempt;
import hello.domain.ThirdPartLogin.Provider;
import hello.util.ControllerUtil;
import hello.vo.BootUserVo;
import hello.vo.LoginAttemptVo;

@Controller
@RequestMapping(path = "/login")
public class LoginController {
	
    public static String LOGIN_ATTEMTPT_REQUEST_KEY = "__LOGIN_ATTEMTPT_REQUEST_KEY";

    public static String RETRY_TIMES_SESSION_KEY = "__RETRY_TIMES_SESSION_KEY";

    public static int MAX_RETRY_BEFORE_SHOW_CAPTCHA = 3;
	
    @Autowired
    private ChangeSessionIdAuthenticationStrategy sessionAuthenticationStrategy;
    
    @Autowired
    private AuthenticationManager am;
    
    @Autowired
    private ControllerUtil cutil;
    
    @Autowired
    private BootUserDetailManager bootUserManager;
	
	@RequestMapping(method = RequestMethod.GET)
	public String loginGet(Model model) {
		model.addAttribute("templateName", "login");
		model.addAttribute("fragmentName", "login");
		model.addAttribute("loginAttempt", new LoginAttemptVo());
		return "index";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String loginPost(final LoginAttemptVo loginAttempt, final BindingResult bindingResult,HttpServletRequest request, HttpServletResponse response, final ModelMap model) {
		model.addAttribute("templateName", "login");
		model.addAttribute("fragmentName", "login");
		v(request, response, loginAttempt);
		return "index";
	}
	
	private void v(HttpServletRequest request, HttpServletResponse response, LoginAttemptVo loginAttemptVo) {
		
	    try {
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginAttemptVo.getUsername(), loginAttemptVo.getPassword());
			
			WebAuthenticationDetails wd = new WebAuthenticationDetailsSource().buildDetails(request);
			authRequest.setDetails(wd);
			
			request.setAttribute(LOGIN_ATTEMTPT_REQUEST_KEY, new LoginAttempt(loginAttemptVo, wd.getRemoteAddress(), wd.getSessionId()));

			Authentication an = am.authenticate(authRequest);
			
			BootUserVo user = bootUserManager.loadUserByUsername(loginAttemptVo.getUsername());

			user = (BootUserVo) an.getPrincipal();

			SecurityContextHolder.getContext().setAuthentication(an);

			cutil.recordLoginAttempt(user.getEmail(), Provider.NORMAL, wd.getRemoteAddress(), true);

			cutil.cleanupRequestAndSession(request, response);

			sessionAuthenticationStrategy.onAuthentication(an, request, response);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
