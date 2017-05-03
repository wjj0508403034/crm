package com.huoyun.core.bo.query.converters;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class DoubleValueConverter extends AbstractValueConverter {
	public DoubleValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	@Override
	public Object converter(String value) throws BusinessException {

		try {
			return Double.parseDouble(value);
		} catch (Exception ex) {
			throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
		}

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
