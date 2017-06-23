package com.huoyun.business.permission;

import java.util.List;

import com.huoyun.exception.BusinessException;

public interface PermissionService {

	void addGroupMember(Long groupId, List<Long> employeeIds) throws BusinessException;

	List<PermissionGroup> getCurrentPermissionGroups() throws BusinessException;

}
