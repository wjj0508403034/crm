package com.huoyun.core.bo.ext.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.ext.UserEntity;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.utils.BusinessObjectUtils;
import com.huoyun.exception.BusinessException;

public class UserEntityImpl implements UserEntity {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserEntityImpl.class);
	public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z");

	private DynamicEntity dynamicEntity;
	private BoMeta boMeta;

	public UserEntityImpl(DynamicEntity de, BoMeta boMeta) {
		this.dynamicEntity = de;
		this.boMeta = boMeta;
	}

	@Override
	public Long getId() {
		return (Long) dynamicEntity.get(BusinessObjectUtils.EXT_TABLE_ID);
	}

	@Override
	public void setId(Long id) {
		dynamicEntity.set(BusinessObjectUtils.EXT_TABLE_ID, id);
	}

	@Override
	public Long getParentId() {
		return (Long) dynamicEntity.get(BusinessObjectUtils.EXT_TABLE_PID);
	}

	@Override
	public void setParentId(Long id) {
		dynamicEntity.set(BusinessObjectUtils.EXT_TABLE_PID, id);
	}

	@Override
	public Object getPropertyValue(String propertyName)
			throws BusinessException {
		if (StringUtils.equals(propertyName, BusinessObjectUtils.EXT_TABLE_ID)) {
			return getId();
		}
		if (StringUtils.equals(propertyName, BusinessObjectUtils.EXT_TABLE_PID)) {
			return this.getParentId();
		}
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

	@Override
	public void setPropertyValue(String propertyName, Object propertyValue)
			throws BusinessException {
		if (StringUtils.equals(propertyName, BusinessObjectUtils.EXT_TABLE_ID)) {
			this.setId((Long) propertyValue);
			return;
		}

		if (StringUtils.equals(propertyName, BusinessObjectUtils.EXT_TABLE_PID)) {
			this.setParentId((Long) propertyValue);
			return;
		}

		PropertyMeta propMeta = this.boMeta.getPropertyMeta(propertyName);
		if (propMeta == null) {
			LOGGER.error(String.format("%s does not exist on the bo %s",
					propertyName, this.boMeta.getName()));
			throw new BusinessException(BoErrorCode.Bo_Property_Not_Exist);
		}

		Object dbValue = this.toDbValue(propertyValue);
		this.dynamicEntity.set(propMeta.getColumnName(), dbValue);

	}
	
	@Override
	public void persist(EntityManager entityManager) {
		entityManager.persist(this.dynamicEntity);
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

	private Object toDbValue(Object value) {
		BigDecimal decimal = null;
		if (value instanceof Integer) {
			decimal = new BigDecimal((Integer) value);
		} else if (value instanceof Long) {
			decimal = new BigDecimal((Long) value);
		} else if (value instanceof Boolean) {
			decimal = ((Boolean) value) ? BigDecimal.ONE : BigDecimal.ZERO;
		} else if (value instanceof Double) {
			decimal = new BigDecimal(String.valueOf(value));
		} else if (value instanceof BigDecimal) {
			decimal = (BigDecimal) value;
		}
		if (decimal != null) {
			return decimal;
		}
		Object obj = value;
		if (value instanceof DateTime) {
			obj = ((DateTime) value).toString(DATE_TIME_FORMATTER);
		} else if (value instanceof String) {
			obj = String.valueOf(value);
		} else if (value instanceof Enum) {
			obj = ((Enum) value).name();
		}
		return obj;
	}



}
