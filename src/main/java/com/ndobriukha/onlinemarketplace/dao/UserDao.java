package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;

import com.ndobriukha.onlinemarketplace.domain.User;

public class UserDao extends GenericDaoHibernateImpl<User, Long> {
	
	public UserDao() {
	}
	
	public UserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
