package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsMobileDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsMobileTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsMobile(String tmpId, JsaHazardsMobileDto jsaHazardsMobileDto) {
		try {
			logger.info("JsaHazardsMobileDto: " + jsaHazardsMobileDto);
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSMOBILE\" VALUES (?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsMobileDto.getMobileEquipment());
			query.setParameter(3, jsaHazardsMobileDto.getAssessEquipmentCondition());
			query.setParameter(4, jsaHazardsMobileDto.getControlAccess());
			query.setParameter(5, jsaHazardsMobileDto.getMonitorProximity());
			query.setParameter(6, jsaHazardsMobileDto.getManageOverheadHazards());
			query.setParameter(7, jsaHazardsMobileDto.getAdhereToRules());
			query.setParameter(8, tmpId);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public JsaHazardsMobileDto getJsaHazardsMobileDto(String permitNum) {
		List<Object[]> obj;
		JsaHazardsMobileDto jsaHazardsMobileDto = new JsaHazardsMobileDto();
		try {

			String sql = "select distinct PERMITNUMBER, MOBILEEQUIPMENT,ASSESSEQUIPMENTCONDITION,CONTROLACCESS, "
					+ " MONITORPROXIMITY,MANAGEOVERHEADHAZARDS,ADHERETORULES from IOP.JSAHAZARDSMOBILE "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsMobileDto.setPermitNumber((Integer) a[0]);
				jsaHazardsMobileDto.setMobileEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMobileDto.setAssessEquipmentCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsMobileDto.setControlAccess(Integer.parseInt(a[3].toString()));
				jsaHazardsMobileDto.setMonitorProximity(Integer.parseInt(a[4].toString()));
				jsaHazardsMobileDto.setManageOverheadHazards(Integer.parseInt(a[5].toString()));
				jsaHazardsMobileDto.setAdhereToRules(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsMobileDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
