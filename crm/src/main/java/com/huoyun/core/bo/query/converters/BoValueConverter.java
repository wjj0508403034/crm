package com.huoyun.core.bo.query.converters;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ErrorCode;
import com.huoyun.exception.BusinessException;

public class BoValueConverter extends AbstractValueConverter {

	public BoValueConverter(PropertyMeta propMeta) {
		super(propMeta);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object converter(String value) throws BusinessException {

		try {
			Long id = Long.parseLong(value);
			Constructor<BusinessObject> constructor = (Constructor<BusinessObject>) this.propMeta.getRuntimeType()
					.getConstructor();
			BusinessObject bo = constructor.newInstance();
			bo.setPropertyValue("id", id);
			return bo;
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
