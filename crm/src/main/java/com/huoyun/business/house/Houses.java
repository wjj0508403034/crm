package com.huoyun.business.house;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.PropertyType;

@BoEntity(allowCustomized = false)
@Entity
@Table
public class Houses extends LiteBusinessObject {

	public Houses() {
	}

	public Houses(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String name;

	@BoProperty(type = PropertyType.Date)
	private DateTime deliveryTime;
	
	@BoProperty
	private String address;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(DateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
