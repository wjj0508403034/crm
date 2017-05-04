package com.huoyun.core.bo.query.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class DateValueConverter extends AbstractDateValueConverter {
	public DateValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeValueConverter.class);

	@Override
	public Object converter(String value) throws BusinessException {
		if (StringUtils.equals(Today, value)) {
			return DateTime.now().withMillisOfDay(0);
		}

		if (StringUtils.equals(Yesterday, value)) {
			return DateTime.now().minusDays(1).withMillisOfDay(0);
		}

		if (StringUtils.equals(Last7Days, value)) {
			return DateTime.now().minusDays(7).withMillisOfDay(0);
		}

		if (StringUtils.equals(Last30Days, value)) {
			return DateTime.now().minusDays(30).withMillisOfDay(0);
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
		if (StringUtils.equals(ThisMonth, value)) {
			values.add(this.getThisMonthBeginDate());
			values.add(this.getThisMonthEndDate());
			return values;
		}

		if (StringUtils.equals(LastMonth, value)) {
			values.add(this.getLastMonthBeginDate());
			values.add(this.getLastMonthEndDate());
			return values;
		}

		for (String item : value.split(",")) {
			values.add(this.converter(item));
		}
		return values;
	}

}
