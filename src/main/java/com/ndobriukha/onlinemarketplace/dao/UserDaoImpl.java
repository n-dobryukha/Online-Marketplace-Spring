package com.ndobriukha.onlinemarketplace.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.ndobriukha.onlinemarketplace.domain.User;

public class UserDaoImpl extends GenericDaoHibernateImpl<User, Long> implements UserDao<User, Long> {

	public UserDaoImpl() {
	}

	public UserDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public User getUserByLogin(String login) {
		return (User) getSession().createCriteria(User.class)
				.add(Restrictions.eq("login", login)).uniqueResult();
	}
}
