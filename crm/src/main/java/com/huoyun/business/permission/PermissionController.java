package com.huoyun.business.permission;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huoyun.business.company.Company;
import com.huoyun.core.bo.BusinessObjectFacade;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {

	@Autowired
	private BusinessObjectFacade boFacade;
	

}
