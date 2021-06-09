package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaStepsTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaStepsTemplate(String id, JsaStepsDto jsaStepsDto) {
		try {
			logger.info("JsaStepsDto: " + jsaStepsDto);
			String sql = "INSERT INTO \"IOP\".\"TMPJSASTEPS\" VALUES (?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, null);
			query.setParameter(3, jsaStepsDto.getTaskSteps());
			query.setParameter(4, jsaStepsDto.getPotentialHazards());
			query.setParameter(5, jsaStepsDto.getHazardControls());
			query.setParameter(6, jsaStepsDto.getPersonResponsible());
			query.setParameter(7, id);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

}
