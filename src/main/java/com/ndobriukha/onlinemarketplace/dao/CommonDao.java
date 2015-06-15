package com.ndobriukha.onlinemarketplace.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.ndobriukha.onlinemarketplace.util.HibernateUtil;

public abstract class CommonDao<T> implements GenericDao<T> {

	Session session = null;
	Transaction transaction = null;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public CommonDao(){
		session = HibernateUtil.getSessionFactory().openSession();
		transaction = session.beginTransaction();
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			Type type = pt.getActualTypeArguments()[0];
			clazz = (Class<T>) type;
		}
	}
	
	public final void commit() {
		transaction.commit();
	}
	
	public final void rollback() {
		transaction.rollback();
	}
	
	public Session getSession() {
		return session;
	}

	@Override
	public void save(T object) throws ConstraintViolationException {
		session.save(object);
		session.flush();
	}	
	
	@Override
	public T get(Long id) {
		T object = null;
		try {
			object = (T) session.get(clazz, id);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return object;
	}

	@Override
	public List<T> getAll() {
		List<T> list = null;
		try {
			list = session.createCriteria(clazz).list();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return list;
	}

	@Override
	public void delete(T object) {
		session.delete(object);
	}
}
