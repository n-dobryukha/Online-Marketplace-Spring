package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;

import com.ndobriukha.onlinemarketplace.domain.Bid;

public class BidDaoImpl extends GenericDaoHibernateImpl<Bid, Long> implements BidDao<Bid, Long> {
	
	public BidDaoImpl() {
	}
	
	public BidDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
