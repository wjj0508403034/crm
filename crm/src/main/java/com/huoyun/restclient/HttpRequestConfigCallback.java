package com.huoyun.restclient;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;

import com.huoyun.web.HttpProxyConfiguration;

public class HttpRequestConfigCallback implements RequestConfigCallback {

	private final static int ConnnectionTimeout = 5000;
	private final static int SocketTimeout = 60000;

	private HttpProxyConfiguration proxyConfig;

	public HttpRequestConfigCallback(HttpProxyConfiguration proxyConfig) {
		this.proxyConfig = proxyConfig;
	}

	@Override
	public Builder customizeRequestConfig(Builder builder) {
		if (this.proxyConfig != null && this.proxyConfig.isEnabled()) {
			HttpHost proxyHost = new HttpHost(this.proxyConfig.getHost(), this.proxyConfig.getPort());
			builder.setProxy(proxyHost);
		}

		builder.setConnectTimeout(ConnnectionTimeout);
		builder.setSocketTimeout(SocketTimeout);
		return builder;
	}

}
