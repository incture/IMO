package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsSubstancesDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsSubstancesTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsSubstancesTemplate(String id, JsaHazardsSubstancesDto jsaHazardsSubstancesDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSSUBSTANCES VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsSubstancesDto.getHazardousSubstances());
			query.setParameter(3, jsaHazardsSubstancesDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSubstancesDto.getFollowSdsControls());
			query.setParameter(5, jsaHazardsSubstancesDto.getImplementHealthHazardControls());
			query.setParameter(6, jsaHazardsSubstancesDto.getTestMaterial());
			query.setParameter(7, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
