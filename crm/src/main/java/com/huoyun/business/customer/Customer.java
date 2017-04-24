package com.huoyun.business.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.employee.Employee;

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
	@BoProperty(label = I18n_Label_Id)
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
	private double depositAmount;

	@BoProperty
	private DateTime contractDate;

	@BoProperty
	private DateTime contractAmount;

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
	private double designCharge;

	@BoProperty
	private double serviceCharge;

	@BoProperty
	private double auxiliaryMaterialCharge;

	@BoProperty
	private double masterMaterialCharge;

	@BoProperty
	private double firstLaborCharge;

	@BoProperty
	private double secondLaborCharge;

	@BoProperty
	private double thirdLaborCharge;

	@BoProperty
	private double otherCharge;

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

	@ManyToOne
	@JoinColumn
	@BoProperty
	private SalesSource salesSource;

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

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public DateTime getContractDate() {
		return contractDate;
	}

	public void setContractDate(DateTime contractDate) {
		this.contractDate = contractDate;
	}

	public DateTime getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(DateTime contractAmount) {
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

	public double getDesignCharge() {
		return designCharge;
	}

	public void setDesignCharge(double designCharge) {
		this.designCharge = designCharge;
	}

	public double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public double getAuxiliaryMaterialCharge() {
		return auxiliaryMaterialCharge;
	}

	public void setAuxiliaryMaterialCharge(double auxiliaryMaterialCharge) {
		this.auxiliaryMaterialCharge = auxiliaryMaterialCharge;
	}

	public double getMasterMaterialCharge() {
		return masterMaterialCharge;
	}

	public void setMasterMaterialCharge(double masterMaterialCharge) {
		this.masterMaterialCharge = masterMaterialCharge;
	}

	public double getFirstLaborCharge() {
		return firstLaborCharge;
	}

	public void setFirstLaborCharge(double firstLaborCharge) {
		this.firstLaborCharge = firstLaborCharge;
	}

	public double getSecondLaborCharge() {
		return secondLaborCharge;
	}

	public void setSecondLaborCharge(double secondLaborCharge) {
		this.secondLaborCharge = secondLaborCharge;
	}

	public double getThirdLaborCharge() {
		return thirdLaborCharge;
	}

	public void setThirdLaborCharge(double thirdLaborCharge) {
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

	public double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(double otherCharge) {
		this.otherCharge = otherCharge;
	}

}
