package com.huoyun.core.bo.metadata;

import com.huoyun.core.bo.BusinessObject;

public interface NodeMeta {

	String getMappedBy();

	Class<BusinessObject> getNodeClass();

}
