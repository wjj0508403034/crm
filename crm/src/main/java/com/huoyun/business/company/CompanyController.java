package com.huoyun.business.company;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;

@Controller
public class CompanyController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/companyInfo", method = RequestMethod.GET)
	@ResponseBody
	public Company getCompanyInfo() {
		String sql = "select t from Company t";
		TypedQuery<Company> query = this.boFacade.getBoRepository(Company.class).newQuery(sql);
		query.setMaxResults(1);
		List<Company> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}
}
