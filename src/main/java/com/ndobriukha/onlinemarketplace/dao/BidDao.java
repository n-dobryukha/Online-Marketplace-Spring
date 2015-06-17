package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;

import com.ndobriukha.onlinemarketplace.domain.Bid;

public class BidDao extends GenericDaoHibernateImpl<Bid, Long> {
	
	public BidDao() {
	}
	
	public BidDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
