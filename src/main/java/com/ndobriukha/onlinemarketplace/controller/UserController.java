package com.ndobriukha.onlinemarketplace.controller;

import java.util.Locale;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getLoginView(Locale locale, Model model) throws Exception {
		return "login";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getRegistrationview(Locale locale, Model model) throws Exception {
		return "registration";
	}
}
