package com.huoyun.core.bo.query.converters;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class StringValueConverter extends AbstractValueConverter {

	public StringValueConverter(PropertyMeta propMeta) {
		super(propMeta);
	}

	@Override
	public Object converter(String value) throws BusinessException {
		if (StringUtils.equals(value, "NULL")) {
			return null;
		}
		
		if (value.startsWith("'") && value.endsWith("'")) {
			return StringUtils.substring(value, 1, value.length() - 1);
		}

		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}

}
