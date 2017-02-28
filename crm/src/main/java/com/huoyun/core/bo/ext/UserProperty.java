package com.huoyun.core.bo.ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Index;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.bo.utils.BusinessObjectUtils;

@BoEntity(allowCustomized = false)
@Entity
@Table(indexes = { @Index(name = "UNIQUSERPROPETY", columnList = "boNamespace,boName,name", unique = true) })
public class UserProperty extends LiteBusinessObject {

	public UserProperty() {

	}

	public UserProperty(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@SequenceGenerator(name = "UserProperty_SEQ", sequenceName = "USER_PROPERTY_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserProperty_SEQ")
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	private String namespace = BusinessObjectUtils.EXTENSION_BO_NAMESPACE;

	private String name;

	private String boNamespace = BusinessObjectUtils.SYSTEM_BO_NAMESPACE;

	private String boName;

	private String label;

	private PropertyType type = PropertyType.String;

	private String description;

	private String customErrorMessage;

	private boolean mandatory;

	private boolean readonly;
	
	private ValidationRule rule;
	
	private String columnName;
	
	private String tableName;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBoNamespace() {
		return boNamespace;
	}

	public void setBoNamespace(String boNamespace) {
		this.boNamespace = boNamespace;
	}

	public String getBoName() {
		return boName;
	}

	public void setBoName(String boName) {
		this.boName = boName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PropertyType getType() {
		return type;
	}

	public void setType(PropertyType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomErrorMessage() {
		return customErrorMessage;
	}

	public void setCustomErrorMessage(String customErrorMessage) {
		this.customErrorMessage = customErrorMessage;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ValidationRule getRule() {
		return rule;
	}

	public void setRule(ValidationRule rule) {
		this.rule = rule;
	}

}
