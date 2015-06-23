package com.ndobriukha.onlinemarketplace.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.ndobriukha.onlinemarketplace.domain.Bid;
import com.ndobriukha.onlinemarketplace.domain.Item;

public class ItemDaoImpl extends GenericDaoHibernateImpl<Item, Long> implements ItemDao<Item, Long> {
	
	public ItemDaoImpl() {
	}
	
	public ItemDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Bid getBestBidByItem(Item item) {
		return (Bid) getSession().createCriteria(Bid.class)
				.add( Restrictions.eq("item", item) )
				.addOrder( Property.forName("amount").desc() )
				.addOrder( Property.forName("id").desc() )
				.setMaxResults(1)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bid> getAllBidsByItem(Item item) {
		return (List<Bid>) getSession().createCriteria(Bid.class)
				.add( Restrictions.eq("item", item) )
				.addOrder( Property.forName("amount").desc() )
				.addOrder( Property.forName("id").desc() )
				.list();
	}
}
