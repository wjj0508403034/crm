package com.huoyun.core.jpa;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class JpaAutoConfiguration extends JpaBaseConfiguration{

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("eclipselink.weaving", "static");
		map.put("eclipselink.logging.parameters", "true");
		map.put("eclipselink.cache.shared.default", "false");
		map.put("eclipselink.jdbc.cache-statements", "true");
		map.put("org.hibernate.flushMode", "COMMIT");
		
		//map.put("eclipselink.ddl-generation", "drop-and-create-tables");
		map.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, "com.huoyun.core.jpa.SessionCustomizerImpl");
		
		return map;
	}
}
