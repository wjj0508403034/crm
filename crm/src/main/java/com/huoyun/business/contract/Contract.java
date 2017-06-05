package com.huoyun.business.contract;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.huoyun.business.customer.Customer;
import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.converters.JodaDateConverter;
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

	@BusinessKey
	@BoProperty(mandatory = true)
	private String contractNo;

	@BoProperty(type = PropertyType.Date, mandatory = true)
	@Convert(JodaDateConverter.Name)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime contractDate;

	@BoProperty(type = PropertyType.Date, mandatory = true)
	@Convert(JodaDateConverter.Name)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime contractBeignDate;

	@BoProperty(type = PropertyType.Date, mandatory = true)
	@Convert(JodaDateConverter.Name)
	@Column(columnDefinition = JodaDateConverter.ColumnDefinition)
	private DateTime contractEndDate;

	@BoProperty(type = PropertyType.Price, mandatory = true, searchable = false, readonly = true)
	private BigDecimal payedAmount;

	@BoProperty(type = PropertyType.Price, mandatory = true, searchable = false, readonly = true)
	private BigDecimal unpayAmount;

	@BoProperty(type = PropertyType.Price, mandatory = true, searchable = false)
	private BigDecimal amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@BoProperty(mandatory = true)
	private Customer customer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee agent;

	@BoProperty(type = PropertyType.Text, searchable = false)
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
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

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public DateTime getContractBeignDate() {
		return contractBeignDate;
	}

	public void setContractBeignDate(DateTime contractBeignDate) {
		this.contractBeignDate = contractBeignDate;
	}

	public DateTime getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(DateTime contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public BigDecimal getPayedAmount() {
		return payedAmount;
	}

	public void setPayedAmount(BigDecimal payedAmount) {
		this.payedAmount = payedAmount;
	}

	public BigDecimal getUnpayAmount() {
		return unpayAmount;
	}

	public void setUnpayAmount(BigDecimal unpayAmount) {
		this.unpayAmount = unpayAmount;
	}

	@Override
	protected void preCreate() {
		this.setPayedAmount(BigDecimal.ZERO);
		this.setUnpayAmount(this.getAmount());
	}

	@Override
	protected void preUpdate() {
		this.getBoFacade().getBean(ContractService.class).beforeAmountChanged(this);
	}

}
