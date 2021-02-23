package com.murphy.taskmgmt.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.murphy.taskmgmt.entity.EmailNotificationMasterDo;

@Transactional
@Repository
public class EmailNotificationMasterDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailNotificationMasterDao.class);
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			logger.error("[Murphy][EmailNotificationMasterDao][getSession][error] " + e.getMessage());
			return sessionFactory.openSession();
		}

	}
	public  List<EmailNotificationMasterDo> getEmailByConfigItemAndRecpType(String item,String type){
		List<EmailNotificationMasterDo> emailList = null;
	
		try{
			String queryString = "from EmailNotificationMasterDo e where e.configItem=:item and e.recpType=:type";
			Query query = this.getSession().createQuery(queryString)
							.setParameter("item", item)
							.setParameter("type", type);
			emailList  = query.list();
			
		}catch(Exception e){
			logger.error("[Murphy][EmailNotificationMasterDao][getByConfigItemAndRecpType][error]" + e.getMessage());

		}
		return emailList;
	}
	
}
