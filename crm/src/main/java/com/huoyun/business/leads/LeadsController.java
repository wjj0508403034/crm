package com.huoyun.business.leads;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.business.customer.Customer;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.BusinessObjectMapper;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/leads")
public class LeadsController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@Autowired
	private BusinessObjectMapper boMapper;

	@RequestMapping(value = "/{leadsId}/generateToCustomer", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateToCustomer(@PathVariable(value = "leadsId") Long leadsId)
			throws BusinessException {
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(Customer.class);
		Customer customer = this.boFacade.getBean(LeadsService.class).generateToCustomer(leadsId);
		return this.boMapper.converterTo(customer, boMeta);
	}
}
