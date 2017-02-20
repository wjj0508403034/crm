package com.huoyun.core.bo.ext.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.exception.BusinessException;

public class UserEntityImpl implements UserEntity {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserEntityImpl.class);

	private final static String User_Defined_Entity_ID = "ID";
	private final static String User_Defined_Entity_Parent_ID = "parent_id";

	private DynamicEntity dynamicEntity;
	private BoMeta boMeta;

	public UserEntityImpl(DynamicEntity de, BoMeta boMeta) {
		this.dynamicEntity = de;
		this.boMeta = boMeta;
	}

	@Override
	public Long getId() {
		return (Long) dynamicEntity.get(User_Defined_Entity_ID);
	}

	@Override
	public void setId(Long id) {
		dynamicEntity.set(User_Defined_Entity_ID, id);
	}

	@Override
	public Long getParentId() {
		return (Long) dynamicEntity.get(User_Defined_Entity_Parent_ID);
	}

	@Override
	public void setParentId(Long id) {
		dynamicEntity.set(User_Defined_Entity_Parent_ID, id);
	}

	@Override
	public Object getPropertyValue(String propertyName)
			throws BusinessException {
		if (StringUtils.equals(propertyName, User_Defined_Entity_ID)) {
			return getId();
		} else if (StringUtils.equals(propertyName,
				User_Defined_Entity_Parent_ID)) {
			return this.getParentId();
		} else {
			PropertyMeta propMeta = this.boMeta.getPropertyMeta(propertyName);
			if (propMeta == null) {
				LOGGER.error(String.format("%s does not exist on the bo %s",
						propertyName, this.boMeta.getName()));
				throw new BusinessException(BoErrorCode.Bo_Property_Not_Exist);
			}

			Object value = dynamicEntity.get(propMeta.getColumnName());
			if (value == null)
				return null;

			return this.fromDbValue(propMeta, value);

		}
	}

	private Object fromDbValue(PropertyMeta propMeta, Object dbValue) {
		if (dbValue instanceof BigDecimal) {
			BigDecimal decimal = (BigDecimal) dbValue;
			if (propMeta.getRuntimeType() == Integer.class) {
				return decimal.intValue();
			}

			if (propMeta.getRuntimeType() == Long.class) {
				return decimal.longValue();
			}
		}

		if (dbValue instanceof String) {
			String str = (String) dbValue;

		}

		return dbValue;
	}
}
