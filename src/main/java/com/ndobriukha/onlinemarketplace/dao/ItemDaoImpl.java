package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;

import com.ndobriukha.onlinemarketplace.domain.Item;

public class ItemDaoImpl extends GenericDaoHibernateImpl<Item, Long> implements ItemDao<Item, Long> {
	
	public ItemDaoImpl() {
	}
	
	public ItemDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
