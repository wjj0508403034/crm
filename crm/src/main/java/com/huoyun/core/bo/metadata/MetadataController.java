package com.huoyun.core.bo.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.business.Contact;
import com.huoyun.core.bo.BusinessObjectFacade;

@Controller
@RequestMapping(value = "/metadata")
public class MetadataController {

	@Autowired
	private MetadataRepository metadataRepository;
	
	@Autowired
	private BusinessObjectFacade boFacade;

	@RequestMapping(value = "/{namespace}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public BoMeta send(@PathVariable(value = "namespace") String namespace,
			@PathVariable(value = "name") String name) {
		Contact contact = this.boFacade.newBo(Contact.class);
		return this.metadataRepository.getBoMeta(namespace, name);
	}
}
