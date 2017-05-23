package com.huoyun.core.bo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
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
import com.huoyun.core.bo.validator.Validator;
import com.huoyun.core.bo.validator.ValidatorFactory;
import com.huoyun.core.converters.JodaDateConverter;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.exception.BusinessException;
import com.huoyun.exception.LocatableBusinessException;

@Converter(name = JodaDateConverter.Name, converterClass = JodaDateConverter.class)
@MappedSuperclass
public abstract class AbstractBusinessObject implements BusinessObject {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBusinessObject.class);

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

	@Transient
	protected ValidatorFactory validatorFactory;

	@Version
	private Long version;

	@Convert(JodaDateConverter.Name)
	@BoProperty(readonly = true, label = I18n_Label_Create_Time, searchable = false)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime createTime;

	@Convert(JodaDateConverter.Name)
	@BoProperty(readonly = true, label = I18n_Label_Update_Time, searchable = false)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime updateTime;

	@Column(name = MultiTenantConstants.CoulmnName, nullable = false, insertable = false, updatable = false)
	private String tenantCode;

	@SuppressWarnings("unchecked")
	@Override
	public void setBoFacade(BusinessObjectFacade boFacade) {
		if (null == this.boFacade) {
			this.boFacade = boFacade;
			this.boRepository = (BoRepository<BusinessObject>) this.boFacade.getBoRepository(this.getClass());
			this.boMeta = this.boFacade.getMetadataRepository().getBoMeta(this.getClass());
			this.validatorFactory = this.boFacade.getValidatorFactory();
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

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	protected void preCreate() throws BusinessException {

	}

	protected void postCreate() throws BusinessException {

	}

	protected void onCreate() {

	}

	protected void onValid() throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(this.getClass());
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			Object propertyValue = this.getPropertyValue(propMeta.getName());
			List<Validator> validators = this.validatorFactory.getValidators(propMeta, propertyValue);
			if (validators != null && validators.size() > 0) {
				for (Validator validator : validators) {
					validator.validator();
				}
			}
		}
	}

	protected void preUpdate() throws BusinessException {

	}

	protected void postUpdate() throws BusinessException {

	}

	protected void preDelete() throws BusinessException {

	}

	protected void postDelete() throws BusinessException {

	}

	protected void onInit() throws BusinessException {

	}

	@Override
	public final void init() throws BusinessException {
		LOGGER.debug("Init bo ...");
		this.onInit();
	}

	@Override
	public final void create() throws BusinessException {
		LOGGER.debug("Start create bo ...");
		this.preCreate();
		this.createTime = DateTime.now();
		this.updateTime = this.createTime;
		this.onValid();
		this.boRepository.save(this);
		this.onCreate();
		this.boRepository.flush();
		this.postCreate();
		LOGGER.debug("End create bo.");
	}

	@Override
	public final void update() throws BusinessException {
		this.preUpdate();
		this.updateTime = DateTime.now();
		this.boRepository.update(this);
		this.boRepository.flush();
		this.postUpdate();
	}

	@Override
	public final void delete() throws BusinessException {
		this.preDelete();
		this.boRepository.delete(this);
		this.postDelete();
	}

	@Override
	public void setPropertyValue(String propertyName, Object propertyValue) throws BusinessException {
		PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(this.getClass(), propertyName);
		if (prop == null) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		Method setter = prop.getWriteMethod();
		if (setter == null) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			setter.invoke(this, mapper.convertValue(propertyValue, prop.getPropertyType()));
		} catch (Exception e) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Set_Value_Failed, propertyName);
		}
	}

	@Override
	public Object getPropertyValue(String propertyName) throws BusinessException {
		PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(this.getClass(), propertyName);
		Method getter = prop.getReadMethod();
		if (getter == null) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}

		try {
			return getter.invoke(this);
		} catch (Exception e) {
			throw new LocatableBusinessException(BoErrorCode.Bo_Property_Not_Exist, propertyName);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BusinessObject> List<T> getNodeList(String nodeName) throws BusinessException {
		return (List<T>) this.getPropertyValue(nodeName);
	}

	@PostLoad
	public void initBoFacade() {
		if (null == this.boFacade) {
			BusinessObjectFacade facade = BusinessObjectFacadeContext.getBoFacade();
			setBoFacade(facade);
		}
	}

}
