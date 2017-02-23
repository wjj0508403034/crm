package com.huoyun.core.bo.ext;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.metadata.BoMeta;

public interface ExtensionService {

	UserEntity createDynamicEntity(BoMeta boMeta);

	UserEntity load(BusinessObject bo, BoMeta boMeta);

	void persist(BusinessObject bo);
}
