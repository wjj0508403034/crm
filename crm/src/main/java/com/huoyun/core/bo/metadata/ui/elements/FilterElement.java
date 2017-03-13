package com.huoyun.core.bo.metadata.ui.elements;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class FilterElement implements Serializable {

	private static final long serialVersionUID = 2782822482781776685L;

	@XmlAttribute(name = "label", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String label;

	@XmlAttribute(name = "expression", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String expression;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
