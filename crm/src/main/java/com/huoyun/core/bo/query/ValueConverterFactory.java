package com.huoyun.core.bo.query;

import org.joda.time.DateTime;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.query.converters.BoValueConverter;
import com.huoyun.core.bo.query.converters.BooleanValueConverter;
import com.huoyun.core.bo.query.converters.DateTimeValueConverter;
import com.huoyun.core.bo.query.converters.DateValueConverter;
import com.huoyun.core.bo.query.converters.DoubleValueConverter;
import com.huoyun.core.bo.query.converters.LongValueConverter;
import com.huoyun.core.bo.query.converters.StringValueConverter;
import com.huoyun.exception.BusinessException;

public class ValueConverterFactory {

	public static ValueConverter getValueConverter(PropertyMeta propMeta) throws BusinessException {
		if (propMeta.getRuntimeType() == String.class) {
			return new StringValueConverter(propMeta);
		}

		if (propMeta.getType() == PropertyType.Date) {
			return new DateValueConverter(propMeta);
		}

		if (propMeta.getRuntimeType() == DateTime.class) {
			return new DateTimeValueConverter(propMeta);
		}

		if (propMeta.getRuntimeType() == Double.class) {
			return new DoubleValueConverter(propMeta);
		}

		if (propMeta.getRuntimeType() == Boolean.class || propMeta.getRuntimeType() == boolean.class) {
			return new BooleanValueConverter(propMeta);
		}

		if (propMeta.getRuntimeType() == Long.class) {
			return new LongValueConverter(propMeta);
		}

		if (propMeta.getType() == PropertyType.BoLabel) {
			return new BoValueConverter(propMeta);
		}

		throw new BusinessException(ErrorCode.Not_Sopport_Value_Converter);
	}
}
