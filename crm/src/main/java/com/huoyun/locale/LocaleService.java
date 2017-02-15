package com.huoyun.locale;

public interface LocaleService {

	String getMessage(String key);
	
	String getErrorMessage(String errorCode);
}
