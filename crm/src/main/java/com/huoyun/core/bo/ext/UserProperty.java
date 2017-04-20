package com.huoyun.core.bo.ext;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
	@GeneratedValue
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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private ValidationRule rule;

	private String columnName;

	private String tableName;
	
    @OneToMany(mappedBy = "userProperty", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id ASC")
    private final List<UserPropertyValidValue> validValues = new ArrayList<>();

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

	public List<UserPropertyValidValue> getValidValues() {
		return validValues;
	}

}
