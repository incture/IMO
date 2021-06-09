package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsVisibilityDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsVisibilityTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsVisibilityTemplate(String id, JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSVISIBILITY\" VALUES (?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsVisibilityDto.getPoorLighting());
			query.setParameter(3, jsaHazardsVisibilityDto.getProvideAlternateLighting());
			query.setParameter(4, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
			query.setParameter(5, jsaHazardsVisibilityDto.getDeferUntilVisibilityImprove());
			query.setParameter(6, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
			query.setParameter(7, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
