package com.ndobriukha.onlinemarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView getLoginView() throws Exception {
		ModelAndView view = new ModelAndView("login");
		return view;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView getRegistrationview() throws Exception {
		ModelAndView view = new ModelAndView("registration");
		return view;
	}
}
