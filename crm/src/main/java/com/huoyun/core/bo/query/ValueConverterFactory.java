package com.huoyun.core.bo.query;

import org.joda.time.DateTime;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.query.converters.BooleanValueConverter;
import com.huoyun.core.bo.query.converters.DateTimeValueConverter;
import com.huoyun.core.bo.query.converters.LongValueConverter;
import com.huoyun.core.bo.query.converters.StringValueConverter;
import com.huoyun.exception.BusinessException;

public class ValueConverterFactory {

	public static ValueConverter getValueConverter(PropertyMeta propMeta) throws BusinessException {
		if (propMeta.getRuntimeType() == String.class) {
			return new StringValueConverter(propMeta);
		} else if (propMeta.getRuntimeType() == DateTime.class) {
			return new DateTimeValueConverter(propMeta);
		} else if (propMeta.getRuntimeType() == Boolean.class) {
			return new BooleanValueConverter(propMeta);
		} else if(propMeta.getRuntimeType() == Long.class){
			return new LongValueConverter(propMeta);
		}
		
		if(propMeta.getType() == PropertyType.BoLabel){
			return new LongValueConverter(propMeta);
		}
		

		throw new BusinessException(ErrorCode.Not_Sopport_Value_Converter);
	}
}
