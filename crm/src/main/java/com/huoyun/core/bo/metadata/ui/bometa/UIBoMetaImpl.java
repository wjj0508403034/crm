package com.huoyun.core.bo.metadata.ui.bometa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.PropertyMeta;
import com.huoyun.core.bo.metadata.ui.UIBoMeta;
import com.huoyun.core.bo.metadata.ui.elements.ColumnElement;
import com.huoyun.core.bo.metadata.ui.elements.PropertyElement;
import com.huoyun.core.bo.metadata.ui.elements.RootElement;
import com.huoyun.core.bo.metadata.ui.elements.SectionElement;
import com.huoyun.core.bo.metadata.ui.elements.SectionPropertyElement;

public class UIBoMetaImpl implements UIBoMeta {

	private static final Logger LOGGER = LoggerFactory.getLogger(UIBoMetaImpl.class);

	private String boName;
	private String boNamespace;
	private String label;
	private String primaryKey;
	private String businessKey;
	private List<UISection> sections = new ArrayList<>();
	private UIListView listview;
	private List<UIProperty> properties = new ArrayList<>();
	private List<UIBoMeta> nodeTypes = new ArrayList<>();

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

	public List<UIBoMeta> getNodeTypes() {
		return nodeTypes;
	}

	public void setNodeTypes(List<UIBoMeta> nodeTypes) {
		this.nodeTypes = nodeTypes;
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
			uiBoMeta.getProperties().add(UIProperty.parse(propMeta, boFacade));
		}
		return uiBoMeta;
	}

	public static UIBoMeta parse(BoMeta boMeta, BusinessObjectFacade boFacade, RootElement element) {
		UIBoMetaImpl uiBoMeta = new UIBoMetaImpl();
		uiBoMeta.setBoName(boMeta.getName());
		uiBoMeta.setBoNamespace(boMeta.getNamespace());
		uiBoMeta.setLabel(boMeta.getLabel());
		uiBoMeta.setBusinessKey(boMeta.getBusinessKey());
		uiBoMeta.setPrimaryKey(boMeta.getPrimaryKey());

		for (SectionElement sectionElement : element.getSections()) {
			UISection section = new UISection();
			if (!StringUtils.isEmpty(sectionElement.getLabelKey())) {
				section.setLabel(boFacade.getLocaleService().getMessage(sectionElement.getLabelKey()));
			}

			for (SectionPropertyElement sectionPropertyElement : sectionElement.getProperties()) {
				if (boMeta.hasProperty(sectionPropertyElement.getRef())) {
					section.getProperties().add(sectionPropertyElement.getRef());
				} else {
					LOGGER.warn("Can't find property ({}) in the bo metadata", sectionPropertyElement.getRef());
				}
			}

			uiBoMeta.getSections().add(section);
		}

		UIListView listView = new UIListView();
		listView.setOrderby(element.getListview().getOrderby());

		if (StringUtils.isEmpty(listView.getOrderby())) {
			listView.setOrderby(boMeta.getPrimaryKey());
		}
		listView.setSortProperty(element.getListview().getSortProperty());

		if (StringUtils.isEmpty(listView.getSortProperty())) {
			listView.setEnableSort(false);
		} else {
			listView.setEnableSort(Boolean.parseBoolean(element.getListview().getEnableSort()));
		}

		uiBoMeta.setListview(listView);
		for (ColumnElement columnElement : element.getListview().getColumns()) {
			if (boMeta.hasProperty(columnElement.getRef())) {
				listView.getProperties().add(columnElement.getRef());
			} else {
				LOGGER.warn("Can't find property ({}) in the bo metadata", columnElement.getRef());
			}
		}

		for (PropertyElement propertyElement : element.getProperties()) {
			if (boMeta.hasProperty(propertyElement.getName())) {
				uiBoMeta.getProperties()
						.add(UIProperty.parse(boMeta.getPropertyMeta(propertyElement.getName()), boFacade));
			} else {
				LOGGER.warn("Can't find property ({}) in the bo metadata", propertyElement.getName());
			}
		}

		return uiBoMeta;
	}

}
