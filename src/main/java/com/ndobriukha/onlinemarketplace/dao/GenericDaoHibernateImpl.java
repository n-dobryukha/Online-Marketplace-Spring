package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class GenericDaoHibernateImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public GenericDaoHibernateImpl() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			clazz = (Class<T>) type;
		}
	}
	
	public GenericDaoHibernateImpl(SessionFactory sessionFactory) {
		this();
		this.sessionFactory = sessionFactory;
	}
	
	protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	/** Create object */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public PK create(T object) throws ConstraintViolationException, HibernateException {
		return (PK) getSession().save(object);		
	}
	
	/** Returns object by id */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public T get(PK id) throws HibernateException {
		return (T) getSession().get(clazz, id);		
	}

	/** Return all objects */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<T> get() throws HibernateException {
		return getSession().createCriteria(clazz).list();
	}
	
	/** Update object */
	@Override
	@Transactional
	public void update(T object) throws HibernateException {
		try {
			getSession().update(object);
		} catch (Exception e) {
			System.out.println(String.format("Exception on update: %s", e.getMessage()));
		}
	};

	/** Delete object */
	@Override
	@Transactional
	public void delete(T object) throws HibernateException {
		getSession().delete(object);
	}
}
