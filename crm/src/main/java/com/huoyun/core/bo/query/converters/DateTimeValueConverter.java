package com.huoyun.core.bo.query.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class DateTimeValueConverter extends AbstractValueConverter {

	public DateTimeValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeValueConverter.class);

	private static final String Now = "now()";
	private static final String Yesterday = "yesterday()";
	private static final String Tomorrow = "tomorrow()";
	public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z");
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Object converter(String value) throws BusinessException {
		if (StringUtils.equals(Now, value)) {
			return DateTime.now();
		}

		if (StringUtils.equals(Yesterday, value)) {
			return DateTime.now().minusDays(1);
		}

		if (StringUtils.equals(Tomorrow, value)) {
			return DateTime.now().plusDays(1);
		}

		/*
		 * '2017-03-09T13:48:51.573Z'
		 */
		if (value.startsWith("'") && value.endsWith("'")) {
			try {
				return DateTime.parse(StringUtils.substring(value, 1, value.length() - 1), DATE_TIME_FORMATTER);
			} catch (Exception ex) {
				LOGGER.warn("Parse value to DateTime failed", ex);
			}
		}

		try {
			Long longValue = Long.parseLong(value);
			return mapper.convertValue(longValue, DateTime.class);
		} catch (Exception ex) {
			LOGGER.warn("Parse value to long failed", ex);
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}

	@Override
	public List<Object> converterToList(String value) throws BusinessException {
		List<Object> values = new ArrayList<>();
		for (String item : value.split(",")) {
			values.add(this.converter(item));
		}
		return values;
	}

}
