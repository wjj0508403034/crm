package com.huoyun.core.bo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class JpaAutoConfiguration extends JpaBaseConfiguration{

	private static String HUOYUN_PERSISTENCE_UNIT = "HuoYun";
	
//	@Primary
//	@Bean
//	public EntityManagerFactory entityManagerFactory(DataSource dataSource){
//        Map<String, Object> props = new HashMap<String, Object>();
//
//        props.put("eclipselink.target-database", "MySQL");
//        props.put("eclipselink.cache.shared.default", "false");
//        //props.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, "com.sap.sbo.ext.entity.OccSessionCustomizer");
//
//        //String key = this.getKey(url, schema);
//        //props.put(PersistenceUnitProperties.SESSION_NAME, key);
//        props.put(PersistenceUnitProperties.MULTITENANT_SHARED_EMF, "true");
//        props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, dataSource);
//		return Persistence.createEntityManagerFactory(null, props);
//	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("eclipselink.weaving", "false");
		map.put("eclipselink.ddl-generation", "drop-and-create-tables");
		
		return map;
	}
}
