package com.huoyun.core.bo;

public interface BusinessObject {

	public Long getId();
	
	void init();
	
	void create();
	
	void update();
	
	void delete();
}
