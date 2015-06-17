package com.ndobriukha.onlinemarketplace.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateUtil {
	 
	private static final SessionFactory sessionFactory = buildSessionFactory();
 
	private static Session session;
	
	private static SessionFactory buildSessionFactory() {
		try {
			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");
			return (SessionFactory) ctx.getBean("sessionFactory");
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
 
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
 
	public static void shutdown() {
		getSessionFactory().close();
	}
	
	public static void openSession()
    {
        session = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        TransactionSynchronizationManager.initSynchronization();
    }
	
	public static Session getSession() {
		if (session == null) {
			openSession();
		};
		return session;
	}
	
	public static void closeSession()
    {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        sessionHolder.getSession().flush();
        sessionHolder.getSession().close();
        SessionFactoryUtils.releaseSession(sessionHolder.getSession(), sessionFactory);
        TransactionSynchronizationManager.clearSynchronization();
    }
	
	public static void restartSession()
    {
        closeSession();
        openSession();
    }
 
}