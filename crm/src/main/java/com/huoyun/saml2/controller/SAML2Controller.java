package com.huoyun.saml2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/sso")
public class SAML2Controller {

	@RequestMapping(value = "/waiting.html", method = RequestMethod.GET)
	public String waiting(Model model) {
		return "sso/waiting";
	}
}
