package com.incture.iopptw.template.repositories;

import java.util.List;

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
	@SuppressWarnings("unchecked")
	public JsaHazardsPersonnelDto getJsaPersonnel(Integer id) {
		List<Object[]> obj;
		JsaHazardsPersonnelDto jsaHazardsPersonnelDto = new JsaHazardsPersonnelDto();
		try {
			String sql = "select distinct PERMITNUMBER, PERSONNEL,PERFORMINDUCTION,MENTORCOACHSUPERVISE, "
					+ " VERIFYCOMPETENCIES,ADDRESSLIMITATIONS,MANAGELANGUAGEBARRIERS,WEARSEATBELTS "
					+ " from IOP.TMPJSAHAZARDSPERSONNEL where TMPID = :id";
			logger.info("JSAHAZARDSPERSONNEL sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsPersonnelDto.setPermitNumber((Integer) a[0]);
				jsaHazardsPersonnelDto.setPersonnel(Integer.parseInt(a[1].toString()));
				jsaHazardsPersonnelDto.setPerformInduction(Integer.parseInt(a[2].toString()));
				jsaHazardsPersonnelDto.setMentorCoachSupervise(Integer.parseInt(a[3].toString()));
				jsaHazardsPersonnelDto.setVerifyCompetencies(Integer.parseInt(a[4].toString()));
				jsaHazardsPersonnelDto.setAddressLimitations(Integer.parseInt(a[5].toString()));
				jsaHazardsPersonnelDto.setManageLanguageBarriers(Integer.parseInt(a[6].toString()));
				jsaHazardsPersonnelDto.setWearSeatBelts(Integer.parseInt(a[7].toString()));
			}
			return jsaHazardsPersonnelDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
