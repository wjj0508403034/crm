package com.huoyun.business.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectMapper;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@Autowired
	private BusinessObjectMapper boMapper;

	@RequestMapping(value = "/{groupId}/addGroupMembers", method = RequestMethod.POST)
	@ResponseBody
	public void addGroupMembers(@PathVariable(value = "groupId") Long groupId, @RequestBody List<Long> employeeIds)
			throws BusinessException {
		this.boFacade.getBean(PermissionService.class).addGroupMember(groupId, employeeIds);
	}

	@RequestMapping(value = "/currentPermissionGroups", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getCurrentPermissionGroups() throws BusinessException {
		List<Map<String, Object>> result = new ArrayList<>();
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(PermissionGroup.class);
		List<PermissionGroup> groups = this.boFacade.getBean(PermissionService.class).getCurrentPermissionGroups();
		for (PermissionGroup group : groups) {
			result.add(this.boMapper.converterTo(group, boMeta));
		}

		return result;
	}
}
