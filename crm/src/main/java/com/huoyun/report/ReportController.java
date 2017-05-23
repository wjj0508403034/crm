package com.huoyun.report;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.multitenant.TenantContext;

@Controller
@RequestMapping(value = "/reports")
public class ReportController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/top5Sales", method = RequestMethod.GET)
	@ResponseBody
	public List top5Sales() {
		final String sql = "SELECT contract.AGENT_ID as employeeId, sum(AMOUNT) as total, employee.userName FROM contract as contract "
				+ "left join .employee as employee on AGENT_ID = employee.id "
				+ "where contract.TENANT_CODE = ? and employee.TENANT_CODE= ? "
				+ "group by contract.AGENT_ID order by total desc";
		Query query = this.boFacade.getEntityManager().createNativeQuery(sql);
		query.setParameter(1, TenantContext.getCurrentTenantCode());
		query.setParameter(2, TenantContext.getCurrentTenantCode());
		query.setMaxResults(5);
		List<Object[]> list = query.getResultList();
		return list;
	}
}
