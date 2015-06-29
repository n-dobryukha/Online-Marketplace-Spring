package com.ndobriukha.onlinemarketplace.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ndobriukha.onlinemarketplace.dao.ItemDao;
import com.ndobriukha.onlinemarketplace.domain.Item;
import com.ndobriukha.onlinemarketplace.domain.User;

@Controller
@RequestMapping("items")
public class ItemController {
	
	@Autowired
	private ItemDao<Item, Long> itemDao;

	@RequestMapping(value = "/show/all/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String showAll(Locale locale, Model model) throws Exception {
		model.addAttribute("type", "All");
		return "show";
	}
	
	@RequestMapping(value = "/show/my/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String showMy(Locale locale, Model model) throws Exception {
		model.addAttribute("type", "My");
		return "show";
	}
	
	@RequestMapping(value = "/new/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView showNew() throws Exception {
		ModelAndView modelAndView = new ModelAndView("edit");
		modelAndView.addObject("actionUrl", "");
		modelAndView.addObject("httpMethod", "POST");
		modelAndView.addObject("item", new Item());
		return modelAndView;
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView showEdit(@PathVariable Long id, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Item item = itemDao.get(id);
		if (item != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = (User) auth.getPrincipal();
			if (item.getSeller().equals(user)) {			
				ModelAndView modelAndView = new ModelAndView("edit");
				modelAndView.addObject("actionUrl", id);
				modelAndView.addObject("httpMethod", "PUT");
				modelAndView.addObject("item", item);
				return modelAndView;
			} else {
				res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
				return null;
			}			
		} else {
			res.sendRedirect( req.getContextPath() + "/items/new");
			return null;
		}
	}
	
	@RequestMapping(value = "/search/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView showSearch() {
		return new ModelAndView("search");
	}
}
