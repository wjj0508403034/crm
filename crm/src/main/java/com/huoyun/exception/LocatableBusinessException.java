package com.huoyun.exception;

public class LocatableBusinessException extends BusinessException {
	private static final long serialVersionUID = 2613942628591575831L;

	private String path;

	public LocatableBusinessException(String errorCode, String path) {
		super(errorCode);
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
