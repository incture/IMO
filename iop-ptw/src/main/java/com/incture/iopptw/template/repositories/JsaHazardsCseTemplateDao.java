package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardscseDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsCseTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsCseTemplate(String id, JsaHazardscseDto jsaHazardscseDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSCSE VALUES (?,?,?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardscseDto.getConfinedSpaceEntry());
			query.setParameter(3, jsaHazardscseDto.getDiscussWorkPractice());
			query.setParameter(4, jsaHazardscseDto.getConductAtmosphericTesting());
			query.setParameter(5, jsaHazardscseDto.getMonitorAccess());
			query.setParameter(6, jsaHazardscseDto.getProtectSurfaces());
			query.setParameter(7, jsaHazardscseDto.getProhibitMobileEngine());
			query.setParameter(8, jsaHazardscseDto.getProvideObserver());
			query.setParameter(9, jsaHazardscseDto.getDevelopRescuePlan());
			query.setParameter(10, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
