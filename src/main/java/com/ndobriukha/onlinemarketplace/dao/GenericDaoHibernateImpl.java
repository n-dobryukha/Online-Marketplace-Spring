package com.ndobriukha.onlinemarketplace.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.ndobriukha.onlinemarketplace.util.HibernateUtil;

@Repository
public abstract class GenericDaoHibernateImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

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
	
	private Session getSession() {
        return HibernateUtil.getSession();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public PK create(T object) throws ConstraintViolationException, HibernateException {
		return (PK) getSession().save(object);		
	}
	
	@Override
	public T get(PK id) throws HibernateException {
		return (T) getSession().get(clazz, id);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> get() throws HibernateException {
		return getSession().createCriteria(clazz).list();
	}
	
	@Override
	public void update(T object) throws HibernateException {
		getSession().update(object);
	};

	@Override
	public void delete(T object) throws HibernateException {
		getSession().delete(object);
	}
}
