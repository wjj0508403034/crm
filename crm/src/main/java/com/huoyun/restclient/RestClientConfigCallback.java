package com.huoyun.restclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

import com.huoyun.web.HttpProxyConfiguration;

public class RestClientConfigCallback implements HttpClientConfigCallback {

	private HttpProxyConfiguration proxyConfig;

	public RestClientConfigCallback(HttpProxyConfiguration proxyConfig) {
		this.proxyConfig = proxyConfig;
	}

	@Override
	public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder builder) {
		if (this.proxyConfig != null && this.proxyConfig.isEnabled()) {
			if (!StringUtils.isEmpty(this.proxyConfig.getUserName())
					&& StringUtils.isEmpty(this.proxyConfig.getPassword())) {
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(proxyConfig.getHost(), proxyConfig.getPort()),
						new UsernamePasswordCredentials(proxyConfig.getUserName(), proxyConfig.getPassword()));

				builder.setDefaultCredentialsProvider(credsProvider);
			}

		}
		return builder;
	}

}
