package com.huoyun.core.bo.metadata.ui;

import java.util.List;

import com.huoyun.core.bo.metadata.ui.bometa.UIListView;
import com.huoyun.core.bo.metadata.ui.bometa.UIProperty;
import com.huoyun.core.bo.metadata.ui.bometa.UISection;

public interface UIBoMeta {

	String getBoName();

	String getBoNamespace();

	String getLabel();

	List<UISection> getSections();

	UIListView getListview();

	List<UIProperty> getProperties();
}
