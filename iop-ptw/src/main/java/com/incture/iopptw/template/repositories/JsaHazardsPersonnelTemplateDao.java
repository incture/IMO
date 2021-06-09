package com.incture.iopptw.template.repositories;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsPersonnelDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsPersonnelTemplateDao extends BaseDao{
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsPersonnelTemplate(String id, JsaHazardsPersonnelDto jsaHazardsPersonnelDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSPERSONNEL VALUES (?,?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsPersonnelDto.getPersonnel());
			query.setParameter(3, jsaHazardsPersonnelDto.getPerformInduction());
			query.setParameter(4, jsaHazardsPersonnelDto.getMentorCoachSupervise());
			query.setParameter(5, jsaHazardsPersonnelDto.getVerifyCompetencies());
			query.setParameter(6, jsaHazardsPersonnelDto.getAddressLimitations());
			query.setParameter(7, jsaHazardsPersonnelDto.getManageLanguageBarriers());
			query.setParameter(8, jsaHazardsPersonnelDto.getWearSeatBelts());
			query.setParameter(9, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
