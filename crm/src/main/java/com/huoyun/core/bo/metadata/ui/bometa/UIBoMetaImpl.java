package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.ui.UIBoMeta;

public class UIBoMetaImpl implements UIBoMeta {

	private String boName;
	private String boNamespace;
	private String label;
	private String primaryKey;
	private String businessKey;
	private List<UISection> sections = new ArrayList<>();
	private UIListView listview;
	private List<UIProperty> properties = new ArrayList<>();

	@Override
	public String getBoName() {
		return boName;
	}

	public void setBoName(String boName) {
		this.boName = boName;
	}

	@Override
	public String getBoNamespace() {
		return boNamespace;
	}

	public void setBoNamespace(String boNamespace) {
		this.boNamespace = boNamespace;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public List<UISection> getSections() {
		return sections;
	}

	public void setSections(List<UISection> sections) {
		this.sections = sections;
	}

	@Override
	public UIListView getListview() {
		return listview;
	}

	public void setListview(UIListView listview) {
		this.listview = listview;
	}

	@Override
	public List<UIProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<UIProperty> properties) {
		this.properties = properties;
	}

	@Override
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public static UIBoMeta parse(BoMeta boMeta, BusinessObjectFacade boFacade) {
		UIBoMetaImpl uiBoMeta = new UIBoMetaImpl();
		uiBoMeta.setBoName(boMeta.getName());
		uiBoMeta.setBoNamespace(boMeta.getNamespace());
		uiBoMeta.setLabel(boMeta.getLabel());
		uiBoMeta.setBusinessKey(boMeta.getBusinessKey());
		uiBoMeta.setPrimaryKey(boMeta.getPrimaryKey());

		UISection section = new UISection();
		uiBoMeta.getSections().add(section);
		section.setLabel(boFacade.getLocaleService().getMessage("uimeta.section.defaultLabel"));

		uiBoMeta.setListview(new UIListView());

		for (PropertyMeta propMeta : boMeta.getProperties()) {
			section.getProperties().add(propMeta.getName());
			uiBoMeta.getListview().getProperties().add(propMeta.getName());
			uiBoMeta.getProperties().add(UIProperty.parse(propMeta,boFacade));
		}
		return uiBoMeta;
	}

}
