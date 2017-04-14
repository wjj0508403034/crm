package com.huoyun.session;

import java.util.Locale;

import com.huoyun.user.UserInfo;

public interface Session {
    public final static String SBO_SESSION_SESS_ATTR = "SBO_SESSION_SESS_ATTR";

    UserInfo getUserInfo();

    Locale getLocale();

    void close();
}
