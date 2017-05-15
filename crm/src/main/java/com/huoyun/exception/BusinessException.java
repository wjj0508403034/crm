package com.huoyun.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = -7679771161029409228L;

	private String code;
	private String message;

	public BusinessException(String errorCode) {
		super(errorCode);
		this.code = errorCode;
	}
	
	public BusinessException(){
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
