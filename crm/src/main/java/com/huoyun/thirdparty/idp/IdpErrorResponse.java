package com.huoyun.thirdparty.idp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huoyun.exception.BusinessException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdpErrorResponse implements Serializable {
	private static final long serialVersionUID = 1298939312232468750L;
	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BusinessException idpException() {
		BusinessException exception = new BusinessException();
		exception.setCode(this.code);
		exception.setMessage(this.message);
		return exception;
	}
}