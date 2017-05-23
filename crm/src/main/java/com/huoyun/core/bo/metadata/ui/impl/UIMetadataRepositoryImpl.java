package com.huoyun.core.bo.metadata.ui.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.ui.TableColumnsParam;
import com.huoyun.core.bo.metadata.ui.UIBoMeta;
import com.huoyun.core.bo.metadata.ui.UIMetaLoader;
import com.huoyun.core.bo.metadata.ui.UIMetadataRepository;
import com.huoyun.core.bo.metadata.ui.bometa.UIBoMetaImpl;
import com.huoyun.core.bo.metadata.ui.elements.RootElement;
import com.huoyun.core.bo.metadata.ui.entity.ListViewColumns;
import com.huoyun.exception.BusinessException;

public class UIMetadataRepositoryImpl implements UIMetadataRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UIMetadataRepositoryImpl.class);

	private UIMetaLoader uiMetaLoader;

	private BusinessObjectFacade boFacade;

	public UIMetadataRepositoryImpl(BusinessObjectFacade boFacade,
			UIMetaLoader uiMetaLoader) {
		this.boFacade = boFacade;
		this.uiMetaLoader = uiMetaLoader;
	}

	@Override
	public UIBoMeta getUIMeta(String namespace, String name)
			throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(
				namespace, name);
		if (boMeta == null) {
			throw new BusinessException(BoErrorCode.Unkown_Bo_Entity);
		}

		UIBoMeta uiBoMeta = null;

		RootElement element = this.uiMetaLoader.getUIMetaElement(namespace,
				name);
		if (element == null) {
			LOGGER.warn("BoNamespace: {}, BoName: {} hasn't xml file.",
					namespace, name);
			uiBoMeta = UIBoMetaImpl.parse(boMeta, this.boFacade);
		} else {
			uiBoMeta = UIBoMetaImpl.parse(boMeta, boFacade, element);
		}

		ListViewColumns listViewColumns = this.getListViewColumns(namespace,
				name);
		if (listViewColumns != null) {
			uiBoMeta.getListview().setProperties(
					listViewColumns.getColumnNames());
		}

		return uiBoMeta;
	}

	@Transactional
	@Override
	public void updateTableColumns(TableColumnsParam tableColumnsParam)
			throws BusinessException {
		ListViewColumns listViewColumns = this.getListViewColumns(
				tableColumnsParam.getBoNamespace(),
				tableColumnsParam.getBoName());

		if (listViewColumns == null) {
			listViewColumns = this.boFacade.newBo(ListViewColumns.class);
			listViewColumns.setBoName(tableColumnsParam.getBoName());
			listViewColumns.setBoNamespace(tableColumnsParam.getBoNamespace());
			listViewColumns.setColumns(tableColumnsParam.getColumns());
			listViewColumns.setEmployee(this.boFacade.getCurrentEmployee());
			listViewColumns.create();
			return;
		}

		listViewColumns.setBoFacade(boFacade);
		listViewColumns.setColumns(tableColumnsParam.getColumns());
		listViewColumns.update();
	}

	private ListViewColumns getListViewColumns(String namespace, String name) {
		final String sql = "select t from ListViewColumns t where t.boName = :boName and t.boNamespace = :boNamespace and t.employee = :employee";
		TypedQuery<ListViewColumns> query = this.boFacade.getBoRepository(
				ListViewColumns.class).newQuery(sql);
		query.setParameter("boName", name);
		query.setParameter("boNamespace", namespace);
		query.setParameter("employee", this.boFacade.getCurrentEmployee());
		List<ListViewColumns> result = query.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}

		return null;
	}

}
