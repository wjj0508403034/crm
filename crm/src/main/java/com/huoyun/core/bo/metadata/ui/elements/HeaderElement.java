package com.huoyun.core.bo.metadata.ui.elements;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class HeaderElement implements Serializable {

	private static final long serialVersionUID = 8863720217935551811L;

	@XmlElement(name = "title")
	private TitleElement title;

	@XmlElement(name = "subtitle")
	private SubTitleElement subTitle;

	public TitleElement getTitle() {
		return title;
	}

	public void setTitle(TitleElement title) {
		this.title = title;
	}

	public SubTitleElement getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(SubTitleElement subTitle) {
		this.subTitle = subTitle;
	}
}
