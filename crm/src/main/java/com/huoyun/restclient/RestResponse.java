package com.huoyun.restclient;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.exception.BusinessException;

public class RestResponse {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponse.class);

	private int statusCode;
	private String body;
	private Map<String, String> headers;
	private final ObjectMapper mapper = new ObjectMapper();

	public <T> T toEntity(Class<T> klass) throws BusinessException {
		if (StringUtils.isEmpty(this.body)) {
			return null;
		}

		try {
			return this.mapper.readValue(this.body, klass);
		} catch (IOException ex) {
			LOGGER.error("Parse {} to type {} failed.", this.body, klass, ex);
			throw new BusinessException(RestClientErrorCode.HTTP_Response_Parse_Failed);
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

}
