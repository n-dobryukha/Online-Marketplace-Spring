package com.ndobriukha.onlinemarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("items")
public class ItemController {

	@RequestMapping("/show/all")
	public ModelAndView showAll() throws Exception {
		ModelAndView model = new ModelAndView("showitems");
		model.addObject("model", "All");
		return model;
	}
	
	@RequestMapping("/show/my")
	public ModelAndView showMy() throws Exception {
		ModelAndView model = new ModelAndView("showitems");
		model.addObject("model", "My");
		return model;
	}
	
	@RequestMapping("/new")
	public ModelAndView showEdit() throws Exception {
		ModelAndView model = new ModelAndView("edititem");
		return model;
	}
}
