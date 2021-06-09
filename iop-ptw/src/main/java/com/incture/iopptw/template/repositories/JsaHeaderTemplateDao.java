package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHeaderTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHeaderTemplate(String id, JsaheaderDto jsaheaderDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHEADER VALUES (?,?,?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("insertJsaHeader sql " + sql);
			query.setParameter(1, null);
			query.setParameter(2, null);
			query.setParameter(3, jsaheaderDto.getHasCWP());
			query.setParameter(4, jsaheaderDto.getHasHWP());
			query.setParameter(5, jsaheaderDto.getHasCSE());
			query.setParameter(6, jsaheaderDto.getTaskDescription());
			query.setParameter(7, jsaheaderDto.getIdentifyMostSeriousPotentialInjury());
			query.setParameter(8, jsaheaderDto.getIsActive());
			query.setParameter(9, id);
			query.setParameter(10, jsaheaderDto.getStatus());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
