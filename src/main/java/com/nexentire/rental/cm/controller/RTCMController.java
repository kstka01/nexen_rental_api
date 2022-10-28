package com.nexentire.rental.cm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RTCMController {

	@RequestMapping(value= {"/", "/index"})
	public String index(Model model) {
		
		return "index";
	}
}
