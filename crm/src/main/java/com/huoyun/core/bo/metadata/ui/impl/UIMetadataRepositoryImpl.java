package com.huoyun.core.bo.metadata.ui.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.ui.UIBoMeta;
import com.huoyun.core.bo.metadata.ui.UIMetaLoader;
import com.huoyun.core.bo.metadata.ui.UIMetadataRepository;
import com.huoyun.core.bo.metadata.ui.bometa.UIBoMetaImpl;
import com.huoyun.core.bo.metadata.ui.elements.RootElement;
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

		RootElement element = this.uiMetaLoader.getUIMetaElement(namespace,
				name);
		if (element == null) {
			LOGGER.warn("BoNamespace: {}, BoName: {} hasn't xml file.",
					namespace, name);
			return UIBoMetaImpl.parse(boMeta, this.boFacade);
		}

		return null;
	}

}
