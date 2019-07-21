package com.search.book.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "login")
public class LoginController {

	@GetMapping("")
	public String login(Model model, String error, String logout) {
		
		if(error != null) {
			model.addAttribute("errorMsg", "");
		}
		
		if(logout != null) {
			model.addAttribute("msg", "");
		}
		
		return "login.html";
	}
	
	
}
