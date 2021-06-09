package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsappeDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsappeTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsappeTemplate(String id, JsappeDto jsappeDto) {
		logger.info("jsappeDto" + jsappeDto);
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSA_PPE\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, null);
			query.setParameter(2, jsappeDto.getHardHat());
			query.setParameter(3, jsappeDto.getSafetyBoot());
			query.setParameter(4, jsappeDto.getGoggles());
			query.setParameter(5, jsappeDto.getFaceShield());
			query.setParameter(6, jsappeDto.getSafetyGlasses());
			query.setParameter(7, jsappeDto.getSingleEar());
			query.setParameter(8, jsappeDto.getDoubleEars());
			query.setParameter(9, jsappeDto.getRespiratorTypeDescription());
			query.setParameter(10, jsappeDto.getNeedSCBA());
			query.setParameter(11, jsappeDto.getNeedDustMask());
			query.setParameter(12, jsappeDto.getCottonGlove());
			query.setParameter(13, jsappeDto.getLeatherGlove());
			query.setParameter(14, jsappeDto.getImpactProtection());
			query.setParameter(15, jsappeDto.getGloveDescription());
			query.setParameter(16, jsappeDto.getChemicalGloveDescription());
			query.setParameter(17, jsappeDto.getFallProtection());
			query.setParameter(18, jsappeDto.getFallRestraint());
			query.setParameter(19, jsappeDto.getChemicalSuit());
			query.setParameter(20, jsappeDto.getApron());
			query.setParameter(21, jsappeDto.getFlameResistantClothing());
			query.setParameter(22, jsappeDto.getOtherPPEDescription());
			query.setParameter(23, jsappeDto.getNeedFoulWeatherGear());
			query.setParameter(24, jsappeDto.getHaveConsentOfTaskLeader());
			query.setParameter(25, jsappeDto.getCompanyOfTaskLeader());
			query.setParameter(26, id);
			query.executeUpdate();
			tx.commit();
			session.close();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
