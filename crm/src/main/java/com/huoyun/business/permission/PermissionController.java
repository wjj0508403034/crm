package com.huoyun.business.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/{groupId}/addGroupMembers", method = RequestMethod.POST)
	@ResponseBody
	public void addGroupMembers(@PathVariable(value = "groupId") Long groupId,
			@RequestBody List<Long> employeeIds) throws BusinessException {
		this.boFacade.getBean(PermissionService.class).addGroupMember(groupId,
				employeeIds);
	}
}
