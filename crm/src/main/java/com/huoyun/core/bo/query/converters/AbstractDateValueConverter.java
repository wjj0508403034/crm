package com.huoyun.core.bo.query.converters;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.DateConstants;

public abstract class AbstractDateValueConverter extends AbstractValueConverter {

	protected static final String Today = DateConstants.Today + "()";
	protected static final String Yesterday = DateConstants.Yesterday + "()";
	protected static final String Last7Days = DateConstants.Last7Days + "()";
	protected static final String Last30Days = DateConstants.Last30Days + "()";
	protected static final String ThisMonth = DateConstants.ThisMonth + "()";
	protected static final String LastMonth = DateConstants.LastMonth + "()";

	protected final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z");
	protected static final ObjectMapper mapper = new ObjectMapper();

	public AbstractDateValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	protected DateTime getThisMonthBeginDate() {
		return DateTime.now().withDayOfMonth(1).withMillisOfDay(0);
	}

	protected DateTime getThisMonthEndDate() {
		return DateTime.now().plusMonths(1).withDayOfMonth(1).withMillisOfDay(0);
	}

	protected DateTime getLastMonthBeginDate() {
		return DateTime.now().minusMonths(1).withDayOfMonth(1).withMillisOfDay(0);
	}

	protected DateTime getLastMonthEndDate() {
		return DateTime.now().withDayOfMonth(1).withMillisOfDay(0);
	}

}
