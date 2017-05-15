package com.huoyun.thirdparty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.restclient.RestClientFactory;
import com.huoyun.thirdparty.idp.IdpClient;
import com.huoyun.thirdparty.idp.IdpHostConfiguration;
import com.huoyun.thirdparty.idp.impl.IdpClientImpl;
import com.huoyun.web.HttpProxyConfiguration;

@Configuration
public class ThirdPartyAutoConfiguration {

	@Bean
	public RestClientFactory restClientFactory(HttpProxyConfiguration proxy) {
		return new RestClientFactory(proxy);
	}

	@Bean
	public IdpClient idpClient(RestClientFactory restClientFactory,IdpHostConfiguration idpHost) {
		return new IdpClientImpl(restClientFactory,idpHost);
	}
}
