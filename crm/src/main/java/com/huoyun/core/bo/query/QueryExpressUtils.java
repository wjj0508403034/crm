package com.huoyun.core.bo.query;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.exception.BusinessException;

public class QueryExpressUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static DateTime parseDate(Object value) throws BusinessException {
		if (value.getClass() == long.class || value.getClass() == int.class
				|| value.getClass() == Long.class
				|| value.getClass() == Integer.class) {
			return mapper.convertValue(value, DateTime.class);
		}

		if (value.getClass() == String.class) {
			String expr = ((String) value).trim();
			if (StringUtils.equals(expr, DateTimeExpress.Now)) {
				return DateTime.now();
			}

			if (StringUtils.equals(expr, DateTimeExpress.LastDay)) {
				return DateTime.now().minusDays(1);
			}

			if (StringUtils.equals(expr, DateTimeExpress.NextDay)) {
				return DateTime.now().plusDays(1);
			}

		}

		throw new BusinessException(BoErrorCode.Bo_Query_Express_Parse_Failed);
	}
}
