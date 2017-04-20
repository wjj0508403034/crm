package com.huoyun.core.bo.metadata.ui;

import com.huoyun.exception.BusinessException;

public interface UIMetadataRepository {

	UIBoMeta getUIMeta(String namespace, String name) throws BusinessException;

}
