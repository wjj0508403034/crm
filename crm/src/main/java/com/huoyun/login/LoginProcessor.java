package com.huoyun.login;

import java.util.Locale;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.session.Session;
import com.huoyun.session.impl.SessionImpl;
import com.huoyun.user.UserInfo;
import com.huoyun.user.impl.UserInfoImpl;

public class LoginProcessor {

	private BusinessObjectFacade boFacade;
	
	public LoginProcessor(BusinessObjectFacade boFacade){
		this.boFacade = boFacade;
	}
	
	public Session process(Long userId) {
		this.boFacade.getMetadataRepository().refresh();
		UserInfo userInfo = new UserInfoImpl(1, 1l, 123456, true, true, true,
				1l);
		return new SessionImpl(userInfo, Locale.CHINA);
	}
}
