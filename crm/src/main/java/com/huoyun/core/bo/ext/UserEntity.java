package com.huoyun.core.bo.ext;

import com.huoyun.exception.BusinessException;

public interface UserEntity {

	public Long getId();

	public void setId(Long id);
	
	public Long getParentId();
	
	public void setParentId(Long id);

	Object getPropertyValue(String propertyName) throws BusinessException;
}
