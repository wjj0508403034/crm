package com.huoyun.business.customer.trace;

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
@RequestMapping(value = "/customer")
public class CustomerTraceRecordController {

	@Autowired
	private BusinessObjectFacade boFacade;

	@Autowired
	private BusinessObjectMapper boMapper;

	@RequestMapping(value = "/{customerId}/postComment", method = RequestMethod.POST)
	@ResponseBody
	public void postComment(@PathVariable(value = "customerId") Long customerId, @RequestBody String comment)
			throws BusinessException {
		this.boFacade.getBean(TraceRecordService.class).postComment(customerId, comment);
	}

	@RequestMapping(value = "/{customerId}/comments", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getComments(@PathVariable(value = "customerId") Long customerId)
			throws BusinessException {

		List<CustomerTraceRecord> records = this.boFacade.getBean(TraceRecordService.class).getComments(customerId);

		List<Map<String, Object>> list = new ArrayList<>();
		BoMeta boMeta = this.boFacade.getMetadataRepository().getBoMeta(CustomerTraceRecord.class);
		for (CustomerTraceRecord record : records) {
			list.add(this.boMapper.converterTo(record, boMeta));
		}
		return list;
	}
}
