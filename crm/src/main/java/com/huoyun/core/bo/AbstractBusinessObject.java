package com.huoyun.core.bo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.validate.Validator;
import com.huoyun.core.bo.validate.ValidatorUtils;
import com.huoyun.core.converters.JodaDateConverter;
import com.huoyun.exception.BusinessException;
import com.huoyun.exception.LocatableBusinessException;

@Converter(name = JodaDateConverter.Name, converterClass = JodaDateConverter.class)
@MappedSuperclass
public abstract class AbstractBusinessObject implements BusinessObject {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBusinessObject.class);

	protected static final String I18n_Label_Id = "common.bo.id";
	protected static final String I18n_Label_Create_Time = "common.bo.createTime";
	protected static final String I18n_Label_Update_Time = "common.bo.updateTime";
	protected static final String I18n_Label_Owner_Code = "common.bo.ownerCode";
	protected static final String I18n_Label_Updator_Code = "common.bo.updateCode";
	protected static final String I18n_Label_Creator_Code = "common.bo.creatorCode";

	@Transient
	protected BusinessObjectFacade boFacade;

	@Transient
	protected BoMeta boMeta;

	@Transient
	protected BoRepository<BusinessObject> boRepository;

	@Version
	private Long version;

	@Convert(JodaDateConverter.Name)
	@BoProperty(readonly = true, label = I18n_Label_Create_Time)
	@Column
	private DateTime createTime = DateTime.now();

	@Convert(JodaDateConverter.Name)
	@BoProperty(readonly = true, label = I18n_Label_Update_Time)
	private DateTime updateTime;

	@SuppressWarnings("unchecked")
	@Override
	public void setBoFacade(BusinessObjectFacade boFacade) {
		if (null == this.boFacade) {
			this.boFacade = boFacade;
			this.boRepository = (BoRepository<BusinessObject>) this.boFacade
					.getBoRepository(this.getClass());
			this.boMeta = this.boFacade.getMetadataRepository().getBoMeta(
					this.getClass());
		}
	}

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

	protected void preCreate() {

	}

	protected void postCreate() {

	}

	protected void onValid() throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(
				this.getClass());
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			Object propertyValue = this.getPropertyValue(propMeta.getName());
			if (!propMeta.isMandatory() && propertyValue == null) {
				continue;
			}

			if (propertyValue == null) {
				throw new LocatableBusinessException(
						BoErrorCode.Bo_Property_Validator_Failed,
						propMeta.getName());
			}

			Validator validator = ValidatorUtils.getValidator(this.boFacade,
					propMeta);
			if (validator != null) {
				validator.validator(propertyValue);
			}
		}
	}

	protected void preUpdate() {

	}

	protected void postUpdate() {

	}

	@Override
	public final void init() {
		LOGGER.debug("Init bo ...");

	}

	@Override
	public final void create() throws BusinessException {
		LOGGER.debug("Start create bo ...");
		this.preCreate();
		this.createTime = DateTime.now();
		this.updateTime = this.createTime;
		this.onValid();
		this.boRepository.save(this);
		this.boRepository.flush();
		this.postCreate();
		LOGGER.debug("End create bo.");
	}

	@Override
	public final void update() {
		this.preUpdate();
		this.updateTime = DateTime.now();
		this.boRepository.update(this);
		this.boRepository.flush();
		this.postUpdate();
	}

	@Override
	public final void delete() {
		this.boRepository.delete(this);
	}

	@Override
	public void setPropertyValue(String propertyName, Object propertyValue)
			throws LocatableBusinessException {
		PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(
				this.getClass(), propertyName);
		if (prop == null) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		Method setter = prop.getWriteMethod();
		if (setter == null) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			setter.invoke(this,
					mapper.convertValue(propertyValue, prop.getPropertyType()));
		} catch (Exception e) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Set_Value_Failed, propertyName);
		}
	}

	@Override
	public Object getPropertyValue(String propertyName)
			throws LocatableBusinessException {
		PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(
				this.getClass(), propertyName);
		Method getter = prop.getReadMethod();
		if (getter == null) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		try {
			return getter.invoke(this);
		} catch (Exception e) {
			throw new LocatableBusinessException(
					BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}
	}
}
