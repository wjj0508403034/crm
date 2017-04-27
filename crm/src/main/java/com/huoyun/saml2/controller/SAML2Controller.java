package com.huoyun.saml2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huoyun.view.ViewConstants;

@Controller
@RequestMapping(value = "/sso")
public class SAML2Controller {

	@RequestMapping(value = "/waiting.html", method = RequestMethod.GET)
	public String getLoginWaitPage() {
		return ViewConstants.View_Login_Processing;
	}

	@RequestMapping(value = "/waiting.html", method = RequestMethod.POST)
	public String postLoginWaitPage() {
		return ViewConstants.View_Login_Processing;
	}
}
