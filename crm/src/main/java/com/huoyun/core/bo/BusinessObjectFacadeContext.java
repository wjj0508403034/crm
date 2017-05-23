package com.huoyun.core.bo;

public class BusinessObjectFacadeContext {

	private static BusinessObjectFacade boFacadeInstance;
	
	public BusinessObjectFacadeContext(BusinessObjectFacade boFacade){
		boFacadeInstance = boFacade;
	}
	
	
	public static BusinessObjectFacade getBoFacade(){
		return boFacadeInstance;
	}
}
