package com.huoyun.business.finishwork;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.house.HouseType;
import com.huoyun.business.house.Houses;
import com.huoyun.core.bo.AbstractBusinessObjectImpl;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;
import com.huoyun.core.bo.annotation.BusinessKey;
import com.huoyun.core.bo.metadata.PropertyType;
import com.huoyun.core.multitenant.MultiTenantConstants;
import com.huoyun.core.multitenant.MultiTenantProperties;

@BoEntity
@Entity
@Table
@Multitenant(value = MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantConstants.CoulmnName, contextProperty = MultiTenantProperties.MULTITENANT_CONTEXT_PROPERTY)
public class FinishWork extends AbstractBusinessObjectImpl {

	public FinishWork() {
	}

	public FinishWork(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@BoProperty(label = I18n_Label_Id, searchable = false)
	private Long id;

	@BusinessKey
	@BoProperty(mandatory = true)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty
	private Employee desginer;

	@BoProperty(type = PropertyType.Price)
	private Double projectCost;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty
	private HouseType houseType;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn
	@BoProperty
	private Houses houses;

	@OneToMany(mappedBy = "finishWork", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("id ASC")
	@BoProperty(type = PropertyType.ImageList)
	private final List<FinishWorkDesignPhoto> designPhotos = new ArrayList<>();

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

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public List<FinishWorkDesignPhoto> getDesignPhotos() {
		return designPhotos;
	}

}
