package com.incture.ptw.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDao {

	@Autowired
	private SessionFactory sessionFactory;

	// Connection
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			return sessionFactory.openSession();
		}
	}

	public StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}

	

}