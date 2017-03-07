package com.huoyun.core.bo.ext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.LiteBusinessObject;
import com.huoyun.core.bo.annotation.BoEntity;
import com.huoyun.core.bo.annotation.BoProperty;

@BoEntity(allowCustomized = false)
@Entity
@Table(indexes = { @Index(name = "UNIQUDEALLOCINFO", columnList = "tableName", unique = true) })
public class UDEAllocInfo extends LiteBusinessObject {

	public UDEAllocInfo() {

	}

	public UDEAllocInfo(BusinessObjectFacade boFacade) {
		super(boFacade);
	}

	@Id
	//@SequenceGenerator(name = "UDEAllocInfo_SEQ", sequenceName = "UDE_ALLOCINFO_SEQ")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UDEAllocInfo_SEQ")
	@GeneratedValue
	@BoProperty(label = I18n_Label_Id)
	private Long id;

	@Column(length = 50)
	private String tableName;
	@Column(length = 4000)
	private String strColumns;
	@Column(length = 4000)
	private String numColumns;
	@Column(length = 255)
	private String txtColumns;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStrColumns() {
		return strColumns;
	}

	public void setStrColumns(String strColumns) {
		this.strColumns = strColumns;
	}

	public String getNumColumns() {
		return numColumns;
	}

	public void setNumColumns(String numColumns) {
		this.numColumns = numColumns;
	}

	public String getTxtColumns() {
		return txtColumns;
	}

	public void setTxtColumns(String txtColumns) {
		this.txtColumns = txtColumns;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String[] getStrColumnArray() {
		if (StringUtils.isEmpty(this.strColumns)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		return this.strColumns.split(",");
	}

	public String[] getNumColumnArray() {
		if (StringUtils.isEmpty(numColumns)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}

		return this.numColumns.split(",");
	}

	public String[] getTxtColumnArray() {
		if (StringUtils.isEmpty(this.txtColumns)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}

		return this.txtColumns.split(",");
	}
}
