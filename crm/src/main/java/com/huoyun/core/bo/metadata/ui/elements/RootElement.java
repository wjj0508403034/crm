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
@XmlRootElement(name = "root")
public class RootElement implements Serializable {

	private static final long serialVersionUID = -329878965330926271L;

	@XmlAttribute(name = "name", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String name;

	@XmlAttribute(name = "namespace", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String namespace;

	@XmlElement(name = "header")
	private HeaderElement header;

	@XmlElementWrapper(name = "sections", required = true)
	@XmlElement(name = "section")
	private List<SectionElement> sections;

	@XmlElement(name = "listview")
	private ListViewElement listview;

	@XmlElementWrapper(name = "filters", required = true)
	@XmlElement(name = "filter")
	private List<FilterElement> filters;

	@XmlElementWrapper(name = "properties", required = true)
	@XmlElement(name = "property")
	private List<PropertyElement> properties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public HeaderElement getHeader() {
		return header;
	}

	public void setHeader(HeaderElement header) {
		this.header = header;
	}

	public List<SectionElement> getSections() {
		return sections;
	}

	public void setSections(List<SectionElement> sections) {
		this.sections = sections;
	}

	public ListViewElement getListview() {
		return listview;
	}

	public void setListview(ListViewElement listview) {
		this.listview = listview;
	}

	public List<FilterElement> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterElement> filters) {
		this.filters = filters;
	}

	public List<PropertyElement> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyElement> properties) {
		this.properties = properties;
	}
}
