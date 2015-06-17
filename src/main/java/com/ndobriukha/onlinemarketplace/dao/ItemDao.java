package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;

import com.ndobriukha.onlinemarketplace.domain.Item;

public class ItemDao extends GenericDaoHibernateImpl<Item, Long> {
	
	public ItemDao() {
	}
	
	public ItemDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
