package com.incture.iopptw.repositories;

import java.util.Date;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("ApproveJsaDao")
public class ApproveJsaDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public Integer approveJsa(String jsaPermitNumber, String status, String approvedBy) {
		try {
			Integer permitNumber;
			String sql1 = "select PERMITNUMBER from IOP.JSAHEADER where JSAPERMITNUMBER=:jsaPermitNumber";
			Query q = getSession().createNativeQuery(sql1);
			q.setParameter("jsaPermitNumber", jsaPermitNumber);
			logger.info("1st sql1 : " + sql1);
			permitNumber = (Integer) q.getSingleResult();
			logger.info("permitNumber : " + permitNumber);

			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String sql2 = "UPDATE IOP.JSAHEADER SET STATUS=:status , ISACTIVE=:isActive where JSAPERMITNUMBER=:jsaPermitNumber ";
			Query q1 = session.createNativeQuery(sql2);
			q1.setParameter("status", status);
			q1.setParameter("isActive", 1);
			q1.setParameter("jsaPermitNumber", jsaPermitNumber);
			logger.info("2nd sql " + sql2);
			q1.executeUpdate();

			String sql3 = "UPDATE IOP.JSAREVIEW SET APPROVEDDATE =:newDate, LASTUPDATEDDATE=:newDate, APPROVEDBY=:approvedBy where PERMITNUMBER=:permitNumber ";
			Query q2 = session.createNativeQuery(sql3);
			logger.info("3rd sql " + sql3);
			Date date = new Date();
			q2.setParameter("newDate", date);
			q2.setParameter("approvedBy", approvedBy);
			q2.setParameter("permitNumber", permitNumber);
			q2.executeUpdate();
			tx.commit();
			session.close();
			return permitNumber;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
