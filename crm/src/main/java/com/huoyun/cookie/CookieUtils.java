package com.huoyun.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	public static final String USER_ATTRIBUTE = "_USER_COOKIE_ATTRIBUTE_";
	public static final String COOKIE_NAME = "TICKET_CODE";
	public static final String COOKIE_USER_LOCALE = "USER_LOCALE";
	public static final int SESSION_TIMEOUT_MINUTE = 30;
	public static final String DEFAULT_SBO_PATH = "/sbo/";
	public static final String DEFAULT_SLD_PATH = "/sld/";
	public static final String JSESSION_COOKIE_NAME = "JSESSIONID";
	public static final String RSESSION_COOKIE_NAME = "RSESSIONID";

	/**
	 * Put object into cookie
	 * 
	 * @param serializedObj
	 *            :Object which to be put into cookie
	 * **/
	public static void addValue(HttpServletResponse response,
			String serializedObj) {

		Cookie cookie = new Cookie(COOKIE_NAME, serializedObj);
		// disable after close the browser

		cookie.setPath(DEFAULT_SBO_PATH);
		cookie.setMaxAge(-1);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		response.addCookie(cookie);
	}

	public static void addUserLocaleCookie(HttpServletResponse response,
			String locale) {

		Cookie cookie = new Cookie(COOKIE_USER_LOCALE, locale);
		// disable after close the browser

		cookie.setPath(DEFAULT_SBO_PATH);
		cookie.setMaxAge(-1);
		cookie.setHttpOnly(false);
		cookie.setSecure(true);

		response.addCookie(cookie);
	}

	public static boolean isExist(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return false;
		}

		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
				return true;
			}
		}

		return false;
	}

	public static boolean removeCookie(HttpServletRequest request,
			HttpServletResponse resp, String path, String cookieName) {
		if (cookieName == null || !isExist(request, cookieName)) {
			return false;
		}

		Cookie cookie = new Cookie(cookieName, "");
		cookie.setMaxAge(0);
		cookie.setPath(path);

		resp.addCookie(cookie);
		return true;
	}
}
