package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsLiftingDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsLiftingTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsLifting(String permitNumber, JsaHazardsLiftingDto jsaHazardsLiftingDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSLIFTING\" VALUES (?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsLiftingDto.getLiftingEquipment());
			query.setParameter(3, jsaHazardsLiftingDto.getConfirmEquipmentCondition());
			query.setParameter(4, jsaHazardsLiftingDto.getObtainApprovalForLifts());
			query.setParameter(5, jsaHazardsLiftingDto.getHaveDocumentedLiftPlan());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsLiftingDto getJsaHazardsLiftingDto(String permitNum) {
		List<Object[]> obj;
		JsaHazardsLiftingDto jsaHazardsLiftingDto = new JsaHazardsLiftingDto();
		try {
			String sql = "select distinct PERMITNUMBER, LIFTINGEQUIPMENT,CONFIRMEQUIPMENTCONDITION, "
					+ " OBTAINAPPROVALFORLIFTS,HAVEDOCUMENTEDLIFTPLAN from IOP.JSAHAZARDSLIFTING "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsLiftingDto.setPermitNumber((Integer) a[0]);
				jsaHazardsLiftingDto.setLiftingEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsLiftingDto.setConfirmEquipmentCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsLiftingDto.setObtainApprovalForLifts(Integer.parseInt(a[3].toString()));
				jsaHazardsLiftingDto.setHaveDocumentedLiftPlan(Integer.parseInt(a[4].toString()));
			}
			return jsaHazardsLiftingDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
