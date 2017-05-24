package com.huoyun.business.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.joda.time.DateTime;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.house.HouseType;
import com.huoyun.business.house.Houses;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.annotation.ValidValue;
import com.huoyun.core.bo.annotation.ValidValues;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.converters.JodaDateConverter;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class Customer extends AbstractBusinessObjectImpl {

	public Customer() {
	}

	public Customer(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BusinessKey
	@BoProperty
	private String name;

	@ValidValues(validValues = { @ValidValue(value = "init"), @ValidValue(value = "processing") })
	@BoProperty(searchable = false)
	private String stage = "init";

	@BoProperty(type=PropertyType.Phone)
	private String telephone;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private SalesSource salesSource;

	@ManyToOne
	@JoinColumn
	@BoProperty(searchable = false)
	private Houses houses;

	@BoProperty(searchable = false)
	private String address;

	@BoProperty
	private String houseArea;

	@ManyToOne
	@JoinColumn
	@BoProperty(searchable = false)
	private HouseType houseType;

	@BoProperty
	@Convert(JodaDateConverter.Name)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime completionDate;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee salesman;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee designer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee cadDrawer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee effectDrawer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private FinishType finishType;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private CustomerStatus status;

	@BoProperty
	private Boolean deleted = false;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employee getSalesman() {
		return salesman;
	}

	public void setSalesman(Employee salesman) {
		this.salesman = salesman;
	}

	public Employee getDesigner() {
		return designer;
	}

	public void setDesigner(Employee designer) {
		this.designer = designer;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	public SalesSource getSalesSource() {
		return salesSource;
	}

	public void setSalesSource(SalesSource salesSource) {
		this.salesSource = salesSource;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	public DateTime getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(DateTime completionDate) {
		this.completionDate = completionDate;
	}

	public FinishType getFinishType() {
		return finishType;
	}

	public void setFinishType(FinishType finishType) {
		this.finishType = finishType;
	}

	public Employee getCadDrawer() {
		return cadDrawer;
	}

	public void setCadDrawer(Employee cadDrawer) {
		this.cadDrawer = cadDrawer;
	}

	public Employee getEffectDrawer() {
		return effectDrawer;
	}

	public void setEffectDrawer(Employee effectDrawer) {
		this.effectDrawer = effectDrawer;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Houses getHouses() {
		return houses;
	}

	public void setHouses(Houses houses) {
		this.houses = houses;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public HouseType getHouseType() {
		return houseType;
	}

	public void setHouseType(HouseType houseType) {
		this.houseType = houseType;
	}

}
