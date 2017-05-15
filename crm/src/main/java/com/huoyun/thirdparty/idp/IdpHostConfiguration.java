package com.huoyun.thirdparty.idp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(locations = "classpath:/META-INF/host.idp.properties", prefix = "host.idp")
@Configuration
public class IdpHostConfiguration {

	private String domain;

	private String prefix;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
