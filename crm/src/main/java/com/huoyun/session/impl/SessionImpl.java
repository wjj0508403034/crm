package com.huoyun.session.impl;

import java.io.Serializable;
import java.util.Locale;

import com.huoyun.session.Session;
import com.huoyun.user.UserInfo;

public class SessionImpl implements Session, Serializable {

	private static final long serialVersionUID = 3645996160627079122L;
	private boolean initialized;
	private UserInfo userInfo;
	private Locale locale;

	public SessionImpl(UserInfo userInfo, Locale locale) {
		if (!initialized) {
			this.userInfo = userInfo;
			this.locale = locale;
			initialized = true;
		} else {
			throw new RuntimeException("Session is already initialized");
		}
	}

	@Override
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public void close() {
	}

}
