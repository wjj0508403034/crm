package com.huoyun.core.bo.ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

@BoEntity(allowCustomized = false)
@Entity
@Table
public class UserPropertyValidValue extends LiteBusinessObject {

	public UserPropertyValidValue() {

	}

	public UserPropertyValidValue(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	private String value;

	private String label;

	private boolean disable;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserProperty userProperty;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProperty getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(UserProperty userProperty) {
		this.userProperty = userProperty;
	}

}
