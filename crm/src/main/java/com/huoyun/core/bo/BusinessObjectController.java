package com.huoyun.core.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class BusinessObjectController {

	@Autowired
	private BusinessObjectService businessObjectService;
	
	@RequestMapping(value = "/bo({namespace},{name})/init", method = RequestMethod.POST)
	@ResponseBody
	public BusinessObject send(@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) {
		return this.businessObjectService.initBo(namespace, name);
	}
}
