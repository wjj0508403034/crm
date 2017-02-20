package com.huoyun.upgrade.core;

import java.io.Serializable;

public class KEYColumn implements Serializable {
	private static final long serialVersionUID = -8262064798623486324L;

	private String name;

	private KEYType keyType;

	private String constraintName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KEYType getKeyType() {
		return keyType;
	}

	public void setKeyType(KEYType keyType) {
		this.keyType = keyType;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
}
