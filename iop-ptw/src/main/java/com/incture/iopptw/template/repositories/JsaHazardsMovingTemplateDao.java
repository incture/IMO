package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsMovingDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsMovingTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsMoving(String id, JsaHazardsMovingDto jsaHazardsMovingDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSMOVING\" VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsMovingDto.getMovingEquipment());
			query.setParameter(3, jsaHazardsMovingDto.getConfirmMachineryIntegrity());
			query.setParameter(4, jsaHazardsMovingDto.getProvideProtectiveBarriers());
			query.setParameter(5, jsaHazardsMovingDto.getObserverToMonitorProximityPeopleAndEquipment());
			query.setParameter(6, jsaHazardsMovingDto.getLockOutEquipment());
			query.setParameter(7, jsaHazardsMovingDto.getDoNotWorkInLineOfFire());
			query.setParameter(8, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsMovingDto getJsaHazardsMovingDto(String permitNum) {
		List<Object[]> obj;
		JsaHazardsMovingDto jsaHazardsMovingDto = new JsaHazardsMovingDto();
		try {
			String sql = "select distinct PERMITNUMBER, MOVINGEQUIPMENT,CONFIRMMACHINERYINTEGRITY, "
					+ " PROVIDEPROTECTIVEBARRIERS,OBSERVERTOMONITORPROXIMITYPEOPLEANDEQUIPMENT,LOCKOUTEQUIPMENT, "
					+ " DONOTWORKINLINEOFFIRE from IOP.JSAHAZARDSMOVING where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsMovingDto.setPermitNumber((Integer) a[0]);
				jsaHazardsMovingDto.setMovingEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMovingDto.setConfirmMachineryIntegrity(Integer.parseInt(a[2].toString()));
				jsaHazardsMovingDto.setProvideProtectiveBarriers(Integer.parseInt(a[3].toString()));
				jsaHazardsMovingDto.setObserverToMonitorProximityPeopleAndEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsMovingDto.setLockOutEquipment(Integer.parseInt(a[5].toString()));
				jsaHazardsMovingDto.setDoNotWorkInLineOfFire(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsMovingDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
