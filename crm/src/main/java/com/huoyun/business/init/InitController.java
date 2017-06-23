package com.huoyun.business.init;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.business.company.Company;
import com.huoyun.business.company.CompanyController;
import com.huoyun.business.employee.Employee;
import com.huoyun.business.employee.EmployeeService;
import com.huoyun.business.permission.PermissionGroup;
import com.huoyun.business.permission.PermissionService;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectService;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/initService")
public class InitController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/getInitData", method = RequestMethod.GET)
	@ResponseBody
	public InitData getInitData() throws BusinessException {
		Employee employee = this.boFacade.getCurrentEmployee();

		InitData initData = new InitData();
		List<PermissionGroup> groups = this.boFacade.getBean(PermissionService.class).getCurrentPermissionGroups();
		for (PermissionGroup group : groups) {
			initData.getPermissionGroups().add(new PermissionGroupData(group));
		}

		initData.setEmployee(this.boFacade.getBean(EmployeeService.class).getEmployeeById(employee.getId()));

		List<Map<String, Object>> customerStatusList = this.boFacade.getBean(BusinessObjectService.class)
				.queryAll("com.huoyun.sbo", "CustomerStatus", null, "orderNo");
		initData.setCustomerStatusList(customerStatusList);

		List<Map<String, Object>> constructionStatusList = this.boFacade.getBean(BusinessObjectService.class)
				.queryAll("com.huoyun.sbo", "ConstructionStatus", null, "orderNo");
		initData.setConstructionStatusList(constructionStatusList);
		Company company = this.boFacade.getBean(CompanyController.class).getCompanyInfo();
		initData.setCompany(company);
		return initData;
	}
}
