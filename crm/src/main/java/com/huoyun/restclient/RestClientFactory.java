package com.huoyun.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.exception.BusinessException;
import com.huoyun.web.HttpProxyConfiguration;
import com.huoyun.web.common.HttpHeaderConstants;
import com.huoyun.web.common.HttpMethodConstants;

/*
 * Rest Client : https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_initialization.html
 */

public class RestClientFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestClientFactory.class);

	private final static int MaxRetryTimeout = 60000;

	private HttpProxyConfiguration proxyConfig;

	public RestClientFactory(HttpProxyConfiguration proxyConfig) {
		this.proxyConfig = proxyConfig;
	}

	private RestClient create(final String domain) {
		return this.create(RestClient.builder(HttpHost.create(domain)));
	}

	@SuppressWarnings("unused")
	private RestClient create(final String hostname, final int port,
			final String scheme) {
		return this.create(RestClient.builder(new HttpHost(hostname, port,
				scheme)));
	}

	public RestResponse post(String domain, String url, Object entity,
			Header... headers) throws BusinessException {
		return this.performRequest(HttpMethodConstants.POST, domain, url,
				entity, headers);
	}
	
	public RestResponse patch(String domain, String url, Object entity,
			Header... headers) throws BusinessException {
		return this.performRequest(HttpMethodConstants.PATCH, domain, url,
				entity, headers);
	}

	public RestResponse delete(String domain, String url, Object entity,
			Header... headers) throws BusinessException {
		return this.performRequest(HttpMethodConstants.DELETE, domain, url,
				entity, headers);
	}

	private RestResponse performRequest(String method, String domain,
			String url, Object entity, Header... headers)
			throws BusinessException {
		long startTime = System.currentTimeMillis();
		RestClient restClient = this.create(domain);
		try {
			LOGGER.info("Sending {} request {}{} ...", method, domain, url);
			Response response = restClient.performRequest(
					method, url, new HashMap<>(),
					this.getPayload(entity), headers);
			LOGGER.info("Sending {} request {}{} successfully.", method,
					domain, url);
			return this.getRestResponse(response);
		} catch (ResponseException ex) {
			LOGGER.warn("Send request failed.", ex);
			return this.getRestResponse(ex.getResponse());
		} catch (IOException ex) {
			LOGGER.error("Send request failed.", ex);
			throw new BusinessException(RestClientErrorCode.HTTP_Request_Failed);
		} finally {
			this.closeRestClient(restClient);
			long endTime = System.currentTimeMillis();
			LOGGER.info("Request {}{},Method: {}, Cost {}ms.", domain, url,
					method, endTime - startTime);
		}
	}

	private void closeRestClient(RestClient restClient)
			throws BusinessException {
		try {
			restClient.close();
		} catch (IOException ex) {
			LOGGER.error("Close rest client failed.", ex);
			throw new BusinessException(
					RestClientErrorCode.Close_Rest_Client_Failed);
		}
	}

	private RestResponse getRestResponse(Response response)
			throws BusinessException {
		RestResponse restResponse = new RestResponse();
		LOGGER.info("Status Code: {}", response.getStatusLine().getStatusCode());
		restResponse.setStatusCode(response.getStatusLine().getStatusCode());
		restResponse.setHeaders(this.getResponseHeaders(response));

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			try {
				String responseBody = EntityUtils.toString(httpEntity, "UTF-8");
				LOGGER.info("Response Body: {}", responseBody);
				restResponse.setBody(responseBody);
			} catch (IOException ex) {
				LOGGER.error("Get rest client response failed.", ex);
				throw new BusinessException(
						RestClientErrorCode.GET_Rest_Client_Response_Failed);
			}

		} else {
			LOGGER.info("Response Body: Empty");
		}

		return restResponse;
	}

	private Map<String, String> getResponseHeaders(Response response) {
		Map<String, String> headers = new HashMap<>();
		LOGGER.info("Headers: ");
		if (response.getHeaders() != null) {
			for (Header header : response.getHeaders()) {
				LOGGER.info("{}:{}", header.getName(), header.getValue());
				headers.put(header.getName(), header.getValue());
			}
		}

		return headers;
	}

	private RestClient create(RestClientBuilder builder) {
		RestClient restClient = builder
				.setHttpClientConfigCallback(
						new RestClientConfigCallback(proxyConfig))
				.setRequestConfigCallback(
						new HttpRequestConfigCallback(proxyConfig))
				.setMaxRetryTimeoutMillis(MaxRetryTimeout)
				.setDefaultHeaders(this.getDefaultHeaders()).build();

		return restClient;
	}

	private Header[] getDefaultHeaders() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader(HttpHeaderConstants.Accept,
				HttpHeaderConstants.JsonFormat));
		headers.add(new BasicHeader(HttpHeaderConstants.ContentType,
				HttpHeaderConstants.JsonFormat));

		return headers.toArray(new Header[headers.size()]);
	}

	private StringEntity getPayload(Object obj) throws BusinessException {
		if (obj == null) {
			LOGGER.info("Payload: Empty");
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			String payload = mapper.writeValueAsString(obj);
			LOGGER.info("Payload: {}", payload);
			return new StringEntity(payload, ContentType.APPLICATION_JSON);
		} catch (JsonProcessingException ex) {
			LOGGER.error("Parse to string entity failed.", ex);
			throw new BusinessException(
					RestClientErrorCode.HTTP_Request_Payload_Parse_Failed);
		}
	}

}
