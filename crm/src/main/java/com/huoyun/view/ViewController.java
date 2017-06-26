package com.huoyun.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root() {
		return "redirect:/index.html";
	}

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String waiting(Model model) {
		return ViewConstants.View_Index;
	}
}
