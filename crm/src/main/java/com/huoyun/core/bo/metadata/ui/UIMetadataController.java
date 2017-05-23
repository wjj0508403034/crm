package com.huoyun.core.bo.metadata.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping(value = "/ui-metadata")
public class UIMetadataController {

	@Autowired
	private UIMetadataRepository uiMetadataRepository;

	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/{namespace}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public UIBoMeta send(@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) throws BusinessException {
		return this.uiMetadataRepository.getUIMeta(namespace, name);
	}

	@RequestMapping(value = "/updateTableColumns", method = RequestMethod.POST)
	@ResponseBody
	public void updateTableColumns(
			@RequestBody TableColumnsParam tableColumnsParam) throws BusinessException{
		this.uiMetadataRepository.updateTableColumns(tableColumnsParam);
	}

}
