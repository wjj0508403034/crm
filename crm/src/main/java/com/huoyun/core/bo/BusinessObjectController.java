package com.huoyun.core.bo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping
public class BusinessObjectController {

	@Autowired
	private BusinessObjectService businessObjectService;

	@RequestMapping(value = "/bo({namespace},{name})", method = RequestMethod.GET)
	@ResponseBody
	public List<BusinessObject> query(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) throws BusinessException{
		return this.businessObjectService.query(namespace, name);
	}

	@RequestMapping(value = "/bo({namespace},{name})/init", method = RequestMethod.GET)
	@ResponseBody
	public BusinessObject init(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) {
		return this.businessObjectService.initBo(namespace, name);
	}

	@RequestMapping(value = "/bo({namespace},{name})/create", method = RequestMethod.POST)
	@ResponseBody
	public BusinessObject create(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@RequestBody Map<String, Object> data) throws BusinessException {
		return this.businessObjectService.createBo(namespace, name, data);
	}

	@RequestMapping(value = "/bo({namespace},{name})/{id}", method = RequestMethod.PATCH)
	@ResponseBody
	public BusinessObject update(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@PathVariable(value = "id") Long id,
			@RequestBody Map<String, Object> data) throws BusinessException {
		return this.businessObjectService.updateBo(namespace, name, id, data);
	}

	@RequestMapping(value = "/bo({namespace},{name})/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BusinessObject load(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@PathVariable(value = "id") Long id) throws BusinessException {
		return this.businessObjectService.load(namespace, name, id);
	}

	@RequestMapping(value = "/bo({namespace},{name})/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@PathVariable(value = "id") Long id) throws BusinessException {
		this.businessObjectService.delete(namespace, name, id);
	}
}
