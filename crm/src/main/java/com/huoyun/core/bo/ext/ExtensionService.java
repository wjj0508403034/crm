package com.huoyun.core.bo.ext;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.ext.controller.CustomFieldParam;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.metadata.events.MetadataChangedPublisherAware;
import com.huoyun.exception.BusinessException;

public interface ExtensionService extends MetadataChangedPublisherAware {

	UserEntity createDynamicEntity(BoMeta boMeta);

	UserEntity load(BusinessObject bo, BoMeta boMeta);

	void persist(BusinessObject bo);

	void createUDF(CustomFieldParam customFieldParam) throws BusinessException;
}
