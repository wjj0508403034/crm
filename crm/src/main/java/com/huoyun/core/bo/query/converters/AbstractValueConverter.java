package com.huoyun.core.bo.query.converters;

import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.query.ValueConverter;

public abstract class AbstractValueConverter implements ValueConverter {

	protected final PropertyMeta propMeta;

	public AbstractValueConverter(PropertyMeta propMeta) {
		this.propMeta = propMeta;
	}
}
