package com.huoyun.core.bo;

import java.util.List;

import com.huoyun.exception.BusinessException;

public interface BusinessObject {

	Long getId();

	void setId(Long id);

	void init() throws BusinessException;

	void create() throws BusinessException;

	void update() throws BusinessException;

	void delete() throws BusinessException;

	void setPropertyValue(String propertyName, Object propertyValue) throws BusinessException;

	void setBoFacade(BusinessObjectFacade boFacade);

	Object getPropertyValue(String propertyName) throws BusinessException;

	<T extends BusinessObject> List<T> getNodeList(String nodeName) throws BusinessException;
}
