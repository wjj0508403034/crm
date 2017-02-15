package com.huoyun.core.bo;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.annotation.BoProperty;

@MappedSuperclass
public abstract class AbstractBusinessObject implements BusinessObject {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBusinessObject.class);

	@Transient
	protected BusinessObjectFacade boFacade;

	@Version
	private Long version;

	@BoProperty(readonly = true, label = "common.bo.createTime")
	private DateTime createTime;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
}
