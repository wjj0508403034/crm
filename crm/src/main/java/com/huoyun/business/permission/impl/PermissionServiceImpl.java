package com.huoyun.business.permission.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.huoyun.business.employee.Employee;
import com.huoyun.business.permission.PermissionGroup;
import com.huoyun.business.permission.PermissionGroupMember;
import com.huoyun.business.permission.PermissionService;
import com.huoyun.core.bo.BoErrorCode;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.utils.ListUtils;
import com.huoyun.exception.BusinessException;

public class PermissionServiceImpl implements PermissionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

	private BusinessObjectFacade boFacade;

	public PermissionServiceImpl(BusinessObjectFacade boFacade) {
		this.boFacade = boFacade;
	}

	@Transactional
	@Override
	public void addGroupMember(Long groupId, List<Long> employeeIds) throws BusinessException {
		PermissionGroup group = this.boFacade.getBoRepository(PermissionGroup.class).load(groupId);
		if (group == null) {
			throw new BusinessException(BoErrorCode.Bo_Record_Not_Found);
		}

		ListUtils.removeDup(employeeIds);

		List<Long> existsEmployeeIds = this.getEmployeeIdsFromGroup(group);

		for (Long employeeId : employeeIds) {
			if (!existsEmployeeIds.contains(employeeId)) {
				Employee employee = this.boFacade.getBoRepository(Employee.class).load(employeeId);
				if (employee != null) {
					PermissionGroupMember member = this.boFacade.newBo(PermissionGroupMember.class);
					member.setEmployee(employee);
					member.setGroup(group);
					member.create();
				} else {
					LOGGER.warn("Can't load employee {}", employeeId);
				}
			}
		}
	}

	@Override
	public List<PermissionGroup> getCurrentPermissionGroups() throws BusinessException {
		final String sql = "select t.group from PermissionGroupMember t where t.employee = :currentEmployee group by t.group";
		Employee currentEmployee = this.boFacade.getCurrentEmployee();
		TypedQuery<PermissionGroup> query = this.boFacade.getEntityManager().createQuery(sql, PermissionGroup.class);
		query.setParameter("currentEmployee", currentEmployee);
		return query.getResultList();
	}

	private List<Long> getEmployeeIdsFromGroup(PermissionGroup group) {
		List<Long> ids = new ArrayList<>();
		for (PermissionGroupMember member : group.getMembers()) {
			ids.add(member.getEmployee().getId());
		}
		return ids;
	}

}
