package com.huoyun.business.finishwork;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.house.HouseType;
import com.huoyun.business.house.Houses;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;

@BoEntity
@Entity
@Table
public class FinishWork extends AbstractBusinessObjectImpl {

	public FinishWork() {
	}

	public FinishWork(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String name;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Employee desginer;

	@BoProperty
	private Double projectCost;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private HouseType houseType;

	@ManyToOne
	@JoinColumn
	@BoProperty
	private Houses houses;

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

	public Employee getDesginer() {
		return desginer;
	}

	public void setDesginer(Employee desginer) {
		this.desginer = desginer;
	}

	public Double getProjectCost() {
		return projectCost;
	}

	public void setProjectCost(Double projectCost) {
		this.projectCost = projectCost;
	}

	public HouseType getHouseType() {
		return houseType;
	}

	public void setHouseType(HouseType houseType) {
		this.houseType = houseType;
	}

	public Houses getHouses() {
		return houses;
	}

	public void setHouses(Houses houses) {
		this.houses = houses;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
