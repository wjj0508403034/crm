package com.huoyun.session;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionManager {

	private static final Map<Integer, Set<HttpSession>> httpSessions = new ConcurrentHashMap<>();
	private static final ThreadLocal<HttpServletRequest> req = new ThreadLocal<>();
	private static final ThreadLocal<HttpServletResponse> res = new ThreadLocal<>();

	public static void setRequest(HttpServletRequest request) {
		req.set(request);
	}

	public static void setResponse(HttpServletResponse response) {
		res.set(response);
	}

	public static HttpServletRequest getRequest() {
		return req.get();
	}

	public static HttpServletResponse getResponse() {
		return res.get();
	}

	public static HttpSession getSession() {
		return req.get().getSession();
	}

	public static Map<Integer, Set<HttpSession>> getHttpsessions() {
		return httpSessions;
	}

	public static void addSession(Integer tenantId, HttpSession httpSession) {
		if (!httpSessions.containsKey(tenantId)) {
			httpSessions.put(tenantId, new HashSet<HttpSession>());
		}

		httpSessions.get(tenantId).add(httpSession);
	}

	public static void removeSession(Integer tenantId, HttpSession httpSession) {
		if (httpSessions.containsKey(tenantId)) {
			httpSessions.get(tenantId).remove(httpSession);
		}
	}
}
