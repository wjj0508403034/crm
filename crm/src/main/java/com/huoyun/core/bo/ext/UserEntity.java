package com.huoyun.core.bo.ext;

import javax.persistence.EntityManager;

import com.huoyun.exception.BusinessException;

public interface UserEntity {

	public Long getId();

	public void setId(Long id);
	
	public Long getParentId();
	
	public void setParentId(Long id);

	Object getPropertyValue(String propertyName) throws BusinessException;
	
	void setPropertyValue(String propertyName, Object propertyValue) throws BusinessException;
	
	void persist(EntityManager entityManager);
}
