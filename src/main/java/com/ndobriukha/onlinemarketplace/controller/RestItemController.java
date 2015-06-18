package com.ndobriukha.onlinemarketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ndobriukha.onlinemarketplace.dao.ItemDao;
import com.ndobriukha.onlinemarketplace.domain.Item;

@RestController
@RequestMapping("/rest/items")
public class RestItemController {

	@Autowired
	private ItemDao<Item, Long> itemDao;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Item> getAllItems() {
		System.out.println("getAllItems");
		List<Item> list = itemDao.get();
		System.out.println(list);
        return list;
    }
	
	@RequestMapping(value = "/my", method = RequestMethod.GET)
    public List<Item> getMyItems() {
        return itemDao.get();
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Item getItem(@PathVariable Long id) {
        return itemDao.get(id);
    }
}
