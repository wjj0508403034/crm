package com.huoyun.core.bo.metadata.ui.elements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "section")
public class SectionElement {

	@XmlElementWrapper(name = "properties", required = true)
	@XmlElement(name = "property")
	private List<SectionPropertyElement> properties;
	
	public List<SectionPropertyElement> getProperties() {
		return properties;
	}

	public void setProperties(List<SectionPropertyElement> properties) {
		this.properties = properties;
	}
}
