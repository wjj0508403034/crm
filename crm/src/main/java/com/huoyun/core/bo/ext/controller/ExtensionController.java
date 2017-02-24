package com.huoyun.core.bo.ext.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.ext.ExtensionService;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/ext")
public class ExtensionController {

	@Autowired
	private ExtensionService cxtensionService;

	@RequestMapping(value = "/customFields", method = RequestMethod.POST)
	@ResponseBody
	public void createUDF(@RequestBody CustomFieldParam customFieldParam) throws BusinessException {
		this.cxtensionService.createUDF(customFieldParam);
	}
}
