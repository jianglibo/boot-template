package hello.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import hello.config.ApplicationConfig;
import hello.controller.LoginController;
import hello.domain.LoginAttempt;
import hello.domain.ThirdPartLogin.Provider;
import hello.repository.LoginAttemptRepository;
import hello.vo.SessionKey;



@Component
public class ControllerUtil {

    public Logger logger = LoggerFactory.getLogger(getClass());
	
    private final LoginAttemptRepository larepo;
    
    private final ApplicationConfig appConfig;
    
    private RequestCache requestCache = new HttpSessionRequestCache();
    
    @Autowired
    public ControllerUtil(LoginAttemptRepository larepo, ApplicationConfig appConfig) {
        this.larepo = larepo;
        this.appConfig = appConfig;
    }
    
    public String getXforwadFor(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        return xff;
    }
	
    
    public String getWtg(HttpServletRequest request, HttpServletResponse response) {
        SavedRequest sr = requestCache.getRequest(request, response);
        requestCache.removeRequest(request, response);
        String url;
        if (sr != null) {
            url = sr.getRedirectUrl();
        } else {
            url = (String) request.getSession().getAttribute(SessionKey.LOGIN_CALLBACK_SESSION_KEY);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("saved url: {}", url);
        }
        
        if (url == null) {
            return null;
        }
        
        UrlTransformer utf = new UrlTransformer(url).changeBaseUrl(appConfig.getOutSideBaseUrl());
        
        url = utf.lastUrl();
        if (logger.isDebugEnabled()) {
            logger.debug("saved url after transform: {}", url);
        }
        return url;
    }
    
    public void recordLoginAttempt(String username, Provider provider, String remoteAddress, boolean success) {
        LoginAttempt la = new LoginAttempt();
        la.setUsername(username);
        la.setProvider(Provider.NORMAL);
        la.setRemoteAddress(remoteAddress);
        la.setSuccess(success);
        larepo.save(la);
    }
    
    public void cleanupRequestAndSession(HttpServletRequest request, HttpServletResponse response) {
        request.removeAttribute(LoginController.LOGIN_ATTEMTPT_REQUEST_KEY);

        HttpSession session = request.getSession(true);

        if (session != null) {
            session.removeAttribute(LoginController.RETRY_TIMES_SESSION_KEY);
        }
	}
}

