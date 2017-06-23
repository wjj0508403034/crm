package com.huoyun.business.init;

import com.huoyun.business.permission.PermissionGroup;

public class PermissionGroupData {

	private String name;
	private String states;

	public PermissionGroupData(PermissionGroup group) {
		this.name = group.getName();
		this.states = group.getStates();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

}
