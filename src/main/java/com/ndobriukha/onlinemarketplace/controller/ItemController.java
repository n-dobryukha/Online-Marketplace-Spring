package com.ndobriukha.onlinemarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("items")
public class ItemController {

	@RequestMapping("/all")
	public ModelAndView showAll() throws Exception {
		ModelAndView model = new ModelAndView("showitems");
		model.addObject("model", "All");
		return model;
	}
	
	@RequestMapping("/my")
	public ModelAndView showMy() throws Exception {
		ModelAndView model = new ModelAndView("showitems");
		model.addObject("model", "My");
		return model;
	}
}
