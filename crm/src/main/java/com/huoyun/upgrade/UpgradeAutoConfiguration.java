package com.huoyun.upgrade;

import java.sql.SQLException;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.huoyun.core.classloader.CachedClassLoader;
import com.huoyun.core.classloader.impl.ClassLoaderAutoConfiguration;
import com.huoyun.upgrade.core.builder.ScriptBuilder;
import com.huoyun.upgrade.core.builder.impl.MySqlScriptBuilder;

@AutoConfigureAfter({ DataSourceAutoConfiguration.class,
		ClassLoaderAutoConfiguration.class })
@Configuration
public class UpgradeAutoConfiguration {

	@Bean
	public SchemaUpdate schemaUpdate(JdbcTemplate jdbcTemplate,
			CachedClassLoader classLoader, ScriptBuilder scriptBuilder)
			throws SQLException {
		return new SchemaUpdate(jdbcTemplate, classLoader, scriptBuilder);
	}

	@Bean
	public ScriptBuilder scriptBuilder() {
		return new MySqlScriptBuilder();
	}
}
