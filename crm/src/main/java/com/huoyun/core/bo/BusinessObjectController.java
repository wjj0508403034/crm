package com.huoyun.core.bo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.query.QueryParam;
import com.huoyun.core.bo.query.parser.ParserService;
import com.huoyun.core.bo.query.parser.impl.filters.Filter;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping
public class BusinessObjectController {

	@Autowired
	private BusinessObjectService businessObjectService;

	@Autowired
	private ParserService parserService;

	@RequestMapping(value = "/bo({namespace},{name})", method = RequestMethod.POST)
	@ResponseBody
	public Page<BusinessObject> query(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestBody QueryParam queryParam) throws BusinessException {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return this.businessObjectService.query(namespace, name, pageable,
				queryParam);
	}

	@RequestMapping(value = "/bo({namespace},{name})/query", method = RequestMethod.GET)
	@ResponseBody
	public Page<BusinessObject> queryx(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@RequestParam(value = Filter.Name, required = false) String $filter,
			@RequestParam(value = "$select", required = false) String select,
			@RequestParam(value = "$pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "$pageSize", required = false, defaultValue = "10") int pageSize)
			throws BusinessException {
		Filter filter = this.parserService.parseFilter($filter);
		filter.parser();
		return null;
	}

	@RequestMapping(value = "/bo({namespace},{name})/count", method = RequestMethod.GET)
	@ResponseBody
	public Long count(@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name,
			@RequestBody QueryParam queryParam) throws BusinessException {
		return this.businessObjectService.count(namespace, name, queryParam);
	}

	@RequestMapping(value = "/bo({namespace},{name})/init", method = RequestMethod.GET)
	@ResponseBody
	public BusinessObject init(
			@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) throws BusinessException {
		return this.businessObjectService.initBo(namespace, name);
	}

	@RequestMapping(value = "/bo({namespace},{name})/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(
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
	public Map<String, Object> load(
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
