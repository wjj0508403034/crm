package com.huoyun.business.customer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.joda.time.DateTime;

import com.huoyun.business.employee.Employee;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.PropertyType;

@BoEntity
@Entity
@Table
public class Customer extends AbstractBusinessObjectImpl {

	public Customer() {
	}

	public Customer(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BusinessKey
	@BoProperty
	private String name;

	@BoProperty
	private String contact;

	@BoProperty
	private String community;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee salesman;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee designer;

	@BoProperty
	private String telephone;

	@BoProperty
	private String houseArea;

	@BoProperty
	private String houseType;

	@BoProperty
	private DateTime completionDate;

	@BoProperty
	private DateTime measureDate;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private FinishType finishType;

	@BoProperty
	private DateTime consultationDate;

	@BoProperty
	private DateTime visitDate;

	@BoProperty
	private DateTime payDepositDate;

	@BoProperty
	private Double depositAmount;

	@BoProperty
	private DateTime contractDate;

	@BoProperty
	private Double contractAmount;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee materialman;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee cadDrawer;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee effectDrawer;

	@BoProperty
	private DateTime businessVisitRemind;

	@BoProperty
	private DateTime designVisitRemind;

	@BoProperty
	private String memo;

	@BoProperty
	private Double designCharge;

	@BoProperty
	private Double serviceCharge;

	@BoProperty
	private Double auxiliaryMaterialCharge;

	@BoProperty
	private Double masterMaterialCharge;

	@BoProperty
	private Double firstLaborCharge;

	@BoProperty
	private Double secondLaborCharge;

	@BoProperty
	private Double thirdLaborCharge;

	@BoProperty
	private Double otherCharge;

	@BoProperty
	private DateTime supervisorDateRemind;

	@BoProperty
	private String contractNo;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee supervisor;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee projectLeader;

	@BoProperty
	private DateTime startDate;

	@BoProperty
	private DateTime endDate;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private CustomerStatus status;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("id ASC")
	@BoProperty(type = PropertyType.BoList)
	private final List<CustomerStatusList> statusList = new ArrayList<>();

	@ManyToOne
	@JoinColumn
	@BoProperty
	private SalesSource salesSource;
	
	@BoProperty
	private Boolean deleted = false;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
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

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public DateTime getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(DateTime completionDate) {
		this.completionDate = completionDate;
	}

	public DateTime getMeasureDate() {
		return measureDate;
	}

	public void setMeasureDate(DateTime measureDate) {
		this.measureDate = measureDate;
	}

	public FinishType getFinishType() {
		return finishType;
	}

	public void setFinishType(FinishType finishType) {
		this.finishType = finishType;
	}

	public DateTime getConsultationDate() {
		return consultationDate;
	}

	public void setConsultationDate(DateTime consultationDate) {
		this.consultationDate = consultationDate;
	}

	public DateTime getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(DateTime visitDate) {
		this.visitDate = visitDate;
	}

	public DateTime getPayDepositDate() {
		return payDepositDate;
	}

	public void setPayDepositDate(DateTime payDepositDate) {
		this.payDepositDate = payDepositDate;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public DateTime getContractDate() {
		return contractDate;
	}

	public void setContractDate(DateTime contractDate) {
		this.contractDate = contractDate;
	}

	public Double getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(Double contractAmount) {
		this.contractAmount = contractAmount;
	}

	public Employee getMaterialman() {
		return materialman;
	}

	public void setMaterialman(Employee materialman) {
		this.materialman = materialman;
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

	public DateTime getBusinessVisitRemind() {
		return businessVisitRemind;
	}

	public void setBusinessVisitRemind(DateTime businessVisitRemind) {
		this.businessVisitRemind = businessVisitRemind;
	}

	public DateTime getDesignVisitRemind() {
		return designVisitRemind;
	}

	public void setDesignVisitRemind(DateTime designVisitRemind) {
		this.designVisitRemind = designVisitRemind;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Double getDesignCharge() {
		return designCharge;
	}

	public void setDesignCharge(Double designCharge) {
		this.designCharge = designCharge;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public Double getAuxiliaryMaterialCharge() {
		return auxiliaryMaterialCharge;
	}

	public void setAuxiliaryMaterialCharge(Double auxiliaryMaterialCharge) {
		this.auxiliaryMaterialCharge = auxiliaryMaterialCharge;
	}

	public Double getMasterMaterialCharge() {
		return masterMaterialCharge;
	}

	public void setMasterMaterialCharge(Double masterMaterialCharge) {
		this.masterMaterialCharge = masterMaterialCharge;
	}

	public Double getFirstLaborCharge() {
		return firstLaborCharge;
	}

	public void setFirstLaborCharge(Double firstLaborCharge) {
		this.firstLaborCharge = firstLaborCharge;
	}

	public Double getSecondLaborCharge() {
		return secondLaborCharge;
	}

	public void setSecondLaborCharge(Double secondLaborCharge) {
		this.secondLaborCharge = secondLaborCharge;
	}

	public Double getThirdLaborCharge() {
		return thirdLaborCharge;
	}

	public void setThirdLaborCharge(Double thirdLaborCharge) {
		this.thirdLaborCharge = thirdLaborCharge;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public DateTime getSupervisorDateRemind() {
		return supervisorDateRemind;
	}

	public void setSupervisorDateRemind(DateTime supervisorDateRemind) {
		this.supervisorDateRemind = supervisorDateRemind;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Employee getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}

	public Employee getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(Employee projectLeader) {
		this.projectLeader = projectLeader;
	}

	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public List<CustomerStatusList> getStatusList() {
		return statusList;
	}

}
