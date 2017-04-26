package com.huoyun.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(locations = "classpath:/META-INF/resourceserver.properties", prefix = "resource.server")
@Configuration
public class ResourceServerProperties {

	private String root;

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

}
