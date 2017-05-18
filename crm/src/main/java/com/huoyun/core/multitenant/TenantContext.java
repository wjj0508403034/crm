package com.huoyun.core.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

	private static Logger LOGGER = LoggerFactory.getLogger(TenantContext.class);
	private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

	public static void setCurrentTenantCode(String tenantCode) {
		LOGGER.info("Set tenant code {} in thread local", tenantCode);
		currentTenant.set(tenantCode);
	}

	public static String getCurrentTenantCode() {
		return currentTenant.get();
	}

	public static void destory() {
		LOGGER.info("Remove tenant code {} in thread local",
				getCurrentTenantCode());
		currentTenant.remove();
	}
}
