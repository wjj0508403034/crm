package com.huoyun.login;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.huoyun.cookie.CookieUtils;
import com.huoyun.saml2.EndpointsConstatns;
import com.huoyun.session.Session;
import com.huoyun.session.SessionManager;
import com.huoyun.session.impl.SessionImpl;
import com.huoyun.user.UserInfo;
import com.huoyun.user.impl.UserInfoImpl;

public class LoginRequiredInterceptor extends HandlerInterceptorAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequiredInterceptor.class);
	

    public static final String USER_TENANT_ID_SESS = "_USER_TENANT_ID_";

    private static final String REMOTE_ADDRESS_KEY = "REMOTE_ADDRESS";
    private static final String USER_ID_KEY = "USER_ID";
    private static final String USER_NAME_KEY = "USER_NAME";

    public static final String APP_SERVER_LOGIN_USERS_ATTR = "APP_SERVER_LOGIN_USERS_ATTR";

    public static final String ATTR_ENABLE_FAIL_FAST = "X-ENABLE-FAIL-FAST";
    public static final String ATTR_REQUEST_TIMEOUT = "X-REQUEST-TIMEOUT";
    public static final String ATTR_REQUEST_ARRIVAL_TIME = "X-REQUEST-ARRIVAL-TIME";
    public static final String KEY_ENABLE_FAIL_FAST = "Global.AppServer.enable_fail_fast";
    public static final String KEY_REQUEST_TIMEOUT = "Global.AppServer.request_timeout";
    public static final String TENANT_ID_SESS_ATTR = "_TENANT_ID_SESS_ATTR_";

	
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        LOGGER.info("Login required interceptor pre-handle start...");
       
        SessionManager.setRequest(request);
        SessionManager.setResponse(response);

        LOGGER.info("Login required interceptor get session information.");
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        
        Object mutex = httpSession.getAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
        Locale locale = (Locale) httpSession.getAttribute("sbo.locale");

        LOGGER.info("Login required interceptor waiting the mutex lock.");
        synchronized (mutex) {
            LOGGER.info("Login required interceptor got the mutex lock.");
            Session session = (Session) httpSession.getAttribute(Session.SBO_SESSION_SESS_ATTR);
            if (session == null || !alreadyInitializedInternalSession(httpSession, session)) {
                if (null != httpSession.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR)) {
                    session = initInternalSession(request, response, httpSession, locale);
                } else {
                    LOGGER.info("[Login] NO authorized user in the session, can't continue with URL: {}",
                            getRequestURL(request));
                    response.setStatus(HttpStatus.SC_LOCKED);
                    return false;
                }
            }

            LOGGER.info("Login required interceptor, internal session already initialized.");
            long id = session.getUserInfo().getId();
            int landscapeId = session.getUserInfo().getLandscapeId();
            String landscapeIdstr = String.valueOf(landscapeId);
            httpSession.setAttribute("$LOGGER_TENANT_ID$", String.valueOf(session.getUserInfo().getTenantId()));
            httpSession.setAttribute("$LOGGER_USER_NAME$", landscapeIdstr);
            // init tenant scope for current thread.
           // tenantScope.setTenant(session.getCurrentUser().getTenantId());
           // userSessionScope.setUserSessionId();

//            if (!validateSupportUser(session, response)) {
//                LOGGER.debug("The support user has been revoked.");
//                return false;
//            }
//
//            if (!processUserValidation(session, response)) {
//                LOGGER.debug("Login required interceptor pre-handle end with user invalid.");
//                return false;
//            }

            LOGGER.info("Login required interceptor pre-handle end.");
            return true;
        }

    }
	
	private Session initInternalSession(HttpServletRequest request, HttpServletResponse response,
            HttpSession httpSession, Locale locale) {
        String landscapeId = httpSession.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR).toString();
        LOGGER.info("[Login] User {} trying to login to OCC with request URL: {}.", landscapeId,
                getRequestURL(request));

        Session session = null;
        try {
            session = openSession(httpSession, locale);
            httpSession.setAttribute(Session.SBO_SESSION_SESS_ATTR, session);
            // add user locale to cookie
            addUserLocaleCookie(session, response);

            SessionManager.addSession(session.getUserInfo().getTenantId(), httpSession);
            LOGGER.info("[Login] User {} login to OCC successfully.", landscapeId);
        } catch (Exception e) {
            LOGGER.error("[Login] Unknown exception for user {}, caused by {}.", landscapeId, e);
            throw e;
        }
        return session;
    }
	
	private Session openSession(HttpSession httpSession, Locale locale) {
        Object tenantIdAttr = httpSession.getAttribute(TENANT_ID_SESS_ATTR);
        int tenantId = (null != tenantIdAttr) ? (int) tenantIdAttr : 0;
        httpSession.setAttribute(USER_TENANT_ID_SESS, tenantId);

        //LoginInfo loginInfo = new LoginInfo();
        //loginInfo.setLandscapeId(
         //       Integer.parseInt((String) httpSession.getAttribute(EndpointsConstatns.SSO_USER_SESSION_ATTR)));
        //loginInfo.setTenantId(tenantId);

//        LOGGER.info("[Login] Trying to open the SBO session(by default to HANA DB) for user {}",
//                loginInfo.getLandscapeId());
        UserInfo userInfo = new UserInfoImpl(1,1l,1,true,true,true,1l);
        Session session = new SessionImpl(userInfo,Locale.CHINA);
        //Session session = userLoginProcessor.processLogin(loginInfo, locale);

        // save the tenant Id in session again since userLoginProcessor.processLogin may derive the new tenant id from
        // email or landscape id
        httpSession.setAttribute(TENANT_ID_SESS_ATTR, 1);

        ServletContext context = httpSession.getServletContext();
        ConcurrentHashMap<String, UserInfo> loginUsers = (ConcurrentHashMap<String, UserInfo>) context
                .getAttribute(APP_SERVER_LOGIN_USERS_ATTR);
        if (loginUsers == null) {
            loginUsers = new ConcurrentHashMap<String, UserInfo>();
            context.setAttribute(APP_SERVER_LOGIN_USERS_ATTR, loginUsers);
        }
        loginUsers.put(httpSession.getId(), session.getUserInfo());

        LOGGER.info("Add application session is to redis server.");
        //redisSessionManager.addApplicationSession(String.valueOf(loginInfo.getLandscapeId()), httpSession.getId());

        LOGGER.info("[Login] SBO session openned successfully for user {}, tenantid {}!", userInfo.getLandscapeId(),
                session.getUserInfo().getTenantId());
        return session;
    }

    @SuppressWarnings("unchecked")
    private boolean alreadyInitializedInternalSession(HttpSession httpSession, Session session) {
        ServletContext context = httpSession.getServletContext();
        ConcurrentHashMap<String, UserInfo> loginUsers = (ConcurrentHashMap<String, UserInfo>) context
                .getAttribute(APP_SERVER_LOGIN_USERS_ATTR);
        if (null != loginUsers && !loginUsers.isEmpty() && loginUsers.containsKey(httpSession.getId())) {
            LOGGER.info("OCC session context already initialized.");
            return true;
        }
        LOGGER.info("OCC session context isn't initialize, will initilize OCC session context.");
        return false;
    }
    
    private String getRequestURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName()
                + ("http".equals(request.getScheme()) && request.getServerPort() == 80
                        || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? ""
                                : ":" + request.getServerPort())
                + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }

    private void addUserLocaleCookie(Session session, HttpServletResponse response) {
        String userLocale = "";
        if (null == session.getLocale()) {
            userLocale = "en_US";
        } else {
            userLocale = session.getLocale().toString();
        }
        CookieUtils.addUserLocaleCookie(response, userLocale);
    }
}
