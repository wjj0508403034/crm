package com.huoyun.core.bo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.exception.LocatableBusinessException;

@MappedSuperclass
public abstract class AbstractBusinessObject implements BusinessObject {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBusinessObject.class);

	@Transient
	protected BusinessObjectFacade boFacade;

	@Version
	private Long version;

	@BoProperty(readonly = true, label = "common.bo.createTime")
	@Column
	private DateTime createTime = DateTime.now();

	@BoProperty(readonly = true, label = "common.bo.updateTime")
	private DateTime updateTime;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}

	public DateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public void init() {
		LOGGER.debug("Init bo ...");

	}

	@Override
	public void create() {
		this.createTime = DateTime.now();
		this.updateTime = this.createTime;
		
	}

	@Override
	public void update() {
		this.updateTime = DateTime.now();
	}

	@Override
	public void setPropertyValue(String propertyName, Object propertyValue) throws LocatableBusinessException {
		PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(this.getClass(), propertyName);
		Method setter = prop.getWriteMethod();
		if (setter == null) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			setter.invoke(this, mapper.convertValue(propertyValue, prop.getPropertyType()));
		} catch (Exception e) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Set_Value_Failed, propertyName);
		}
	}
}
