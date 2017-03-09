package com.huoyun.core.bo.query.converters;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class BooleanValueConverter extends AbstractValueConverter {

	public BooleanValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	private final static String TrueValue = "true";
	private final static String FalseValue = "false";

	@Override
	public Object converter(String value) throws BusinessException {
		if (StringUtils.equalsIgnoreCase(FalseValue, value)) {
			return false;
		}

		if (StringUtils.equalsIgnoreCase(TrueValue, value)) {
			return true;
		}
		throw new BusinessException(ErrorCode.Query_Expression_Parse_Failed);
	}

	@Override
	public List<Object> converterToList(String value) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
