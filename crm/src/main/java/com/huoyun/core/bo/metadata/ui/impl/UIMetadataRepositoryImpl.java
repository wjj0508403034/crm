package com.huoyun.core.bo.metadata.ui.impl;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.ui.UIMetaLoader;
import com.huoyun.core.bo.metadata.ui.UIMetadataRepository;

public class UIMetadataRepositoryImpl implements UIMetadataRepository {

	private UIMetaLoader uiMetaLoader;

	private BusinessObjectFacade boFacade;

	public UIMetadataRepositoryImpl(BusinessObjectFacade boFacade,
			UIMetaLoader uiMetaLoader) {
		this.boFacade = boFacade;
		this.uiMetaLoader = uiMetaLoader;
	}
	
	
}
