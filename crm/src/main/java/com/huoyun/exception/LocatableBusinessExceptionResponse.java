package com.huoyun.exception;

public class LocatableBusinessExceptionResponse extends BusinessExceptionResponse {
	private static final long serialVersionUID = -3772016544310283593L;

	private String path;

	public LocatableBusinessExceptionResponse(LocatableBusinessException ex) {
		super(ex);
		this.path = ex.getPath();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
