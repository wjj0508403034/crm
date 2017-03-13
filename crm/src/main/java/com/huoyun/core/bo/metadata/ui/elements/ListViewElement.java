package com.huoyun.core.bo.metadata.ui.elements;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listview")
public class ListViewElement implements Serializable {

	private static final long serialVersionUID = -277779374034041030L;

	@XmlAttribute(name = "select", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String select;

	@XmlAttribute(name = "orderby", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String orderby;

	@XmlElementWrapper(name = "columns", required = true)
	@XmlElement(name = "column")
	private List<ColumnElement> columns;

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public List<ColumnElement> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnElement> columns) {
		this.columns = columns;
	}
}
