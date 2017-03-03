package hello.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hello.domain.LoginAttempt;
import hello.domain.ThirdPartLogin.Provider;
import hello.model.MenuItem;
import hello.model.MenuItem.ShowMenuWhen;
import hello.model.MenuItems;
import hello.util.ControllerUtil;
import hello.vo.BootUserAuthentication;
import hello.vo.BootUserVo;
import hello.vo.LoginAttemptForm;

@Controller
public class LoginController implements HasMenuItemController {
	
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
	private MenuItems menuItemFactory;
	
	@ModelAttribute(name="mainMenus")
	public List<MenuItem> addMenuItemsToModel() {
		return menuItemFactory.CloneMenuItems("login");
	}
	
    
    @ModelAttribute
    public void getLoginAttemptForm(Model model) {
    	model.addAttribute("loginAttemptForm", new LoginAttemptForm());
    }
    
    private void decoModel(Model model, String frgTpl) {
		model.addAttribute("frgTpl", frgTpl);
		model.addAttribute("frgName", frgTpl);
    }
    
	@RequestMapping(path="/login", method = RequestMethod.GET)
	public String loginGet(Model model) {
		decoModel(model, "login");
		return "index";
	}

	@RequestMapping(path="/login", method = RequestMethod.POST)
	public String loginPost(@Valid final LoginAttemptForm loginAttemptForm, final BindingResult bindingResult,HttpServletRequest request, HttpServletResponse response, final Model model) {
		if (v(request, response, loginAttemptForm)) {
			return "redirect:/";
		} else {
			decoModel(model, "login");
			bindingResult.reject("ddd", "validate.login.username");
			bindingResult.rejectValue("username", "validate.login.username");
			decoModel(model, "login");
		}
		return "index";
	}
	
	private boolean v(HttpServletRequest request, HttpServletResponse response, LoginAttemptForm loginAttemptForm) {
		
	    try {
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginAttemptForm.getUsername(), loginAttemptForm.getPassword());
			
			WebAuthenticationDetails wd = new WebAuthenticationDetailsSource().buildDetails(request);
			authRequest.setDetails(wd);
			
			request.setAttribute(LOGIN_ATTEMTPT_REQUEST_KEY, new LoginAttempt(loginAttemptForm, wd.getRemoteAddress(), wd.getSessionId()));

			Authentication an = am.authenticate(authRequest);
			
			BootUserVo user = (BootUserVo) an.getPrincipal();
			
			BootUserAuthentication buan = new BootUserAuthentication(user);

			SecurityContextHolder.getContext().setAuthentication(buan);

			cutil.recordLoginAttempt(user.getEmail(), Provider.NORMAL, wd.getRemoteAddress(), true);
			cutil.cleanupRequestAndSession(request, response);
			sessionAuthenticationStrategy.onAuthentication(buan, request, response);
			return true;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<MenuItem> getMenuItems() {
		MenuItem logout = new MenuItem("logout", "/logout", "menu-item-divide", ShowMenuWhen.LOGINED);
		logout.setDomId("logoutMenuItem");
		List<MenuItem> mis = new ArrayList<>();
		mis.add(new MenuItem("login","/login", "menu-item-divide", ShowMenuWhen.NOT_LOGIN));
		mis.add(logout);
		return mis;
	}
}
