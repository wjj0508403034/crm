package com.huoyun.report;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;

@Controller
@RequestMapping(value = "/reports")
public class ReportController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/top5Sales", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> top5Sales() {
		final String sql = "select count(t), t.salesman.userName from Customer t group by t.salesman order by count(t) desc";
		TypedQuery<Object[]> query = this.boFacade.getEntityManager().createQuery(sql, Object[].class);
		query.setMaxResults(5);
		List<Object[]> list = query.getResultList();
		return list;
	}

	@RequestMapping(value = "/top5Designers", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> top5Designers() {
		final String sql = "select count(t), t.designer.userName from Customer t group by t.designer order by count(t) desc";
		TypedQuery<Object[]> query = this.boFacade.getEntityManager().createQuery(sql, Object[].class);
		query.setMaxResults(5);
		List<Object[]> list = query.getResultList();
		return list;
	}

	@RequestMapping(value = "/contractsOfThisYear", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> contractsOfThisYear() {
		final String sql = "select FUNC('MONTH', t.contractDate), count(t) from Contract t group by FUNC('MONTH', t.contractDate) order by FUNC('MONTH', t.contractDate)";
		TypedQuery<Object[]> query = this.boFacade.getEntityManager().createQuery(sql, Object[].class);
		List<Object[]> list = query.getResultList();
		return list;
	}

	@RequestMapping(value = "/perMonthContractTotalAmountOfThisYear", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> perMonthContractTotalAmountOfThisYear() {
		final String sql = "select FUNC('MONTH', t.contractDate), sum(t.amount), sum(t.payedAmount) from Contract t group by FUNC('MONTH', t.contractDate) order by FUNC('MONTH', t.contractDate)";
		TypedQuery<Object[]> query = this.boFacade.getEntityManager().createQuery(sql, Object[].class);
		List<Object[]> list = query.getResultList();
		return list;
	}

}
