package com.huoyun.business.contract;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.joda.time.DateTime;

import com.huoyun.business.customer.Customer;
import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class Contract extends AbstractBusinessObjectImpl {

	public Contract() {
	}

	public Contract(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BoProperty
	private String contractNo;

	@BoProperty(type = PropertyType.Date)
	private DateTime contractDate;

	@BoProperty(type = PropertyType.Price)
	private Double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@BoProperty
	private Customer customer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee agent;
	
	@BoProperty(type = PropertyType.Text)
	private String memo;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public DateTime getContractDate() {
		return contractDate;
	}

	public void setContractDate(DateTime contractDate) {
		this.contractDate = contractDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getAgent() {
		return agent;
	}

	public void setAgent(Employee agent) {
		this.agent = agent;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
