package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.ui.UIBoMeta;
import com.huoyun.locale.LocaleService;

public class UIBoMetaImpl implements UIBoMeta {

	private String boName;
	private String boNamespace;
	private String label;
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

	public static UIBoMeta parse(BoMeta boMeta, LocaleService localeService) {
		UIBoMetaImpl uiBoMeta = new UIBoMetaImpl();
		uiBoMeta.setBoName(boMeta.getName());
		uiBoMeta.setBoNamespace(boMeta.getNamespace());
		uiBoMeta.setLabel(boMeta.getLabel());
		
		UISection section = new UISection();
		uiBoMeta.getSections().add(section);
		section.setLabel(localeService
				.getMessage("uimeta.section.defaultLabel"));
		
		uiBoMeta.setListview(new UIListView());
		
		for (PropertyMeta propMeta : boMeta.getProperties()) {
			section.getProperties().add(propMeta.getName());
			uiBoMeta.getListview().getProperties().add(propMeta.getName());
			uiBoMeta.getProperties().add(UIProperty.parse(propMeta));
		}
		return uiBoMeta;
	}

}
