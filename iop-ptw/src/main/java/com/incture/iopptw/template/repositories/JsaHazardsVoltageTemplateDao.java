package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsVoltageDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsVoltageTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsVoltage(String tmpId, JsaHazardsVoltageDto jsaHazardsVoltageDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSVOLTAGE\" VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsVoltageDto.getHighVoltage());
			query.setParameter(3, jsaHazardsVoltageDto.getRestrictAccess());
			query.setParameter(4, jsaHazardsVoltageDto.getDischargeEquipment());
			query.setParameter(5, jsaHazardsVoltageDto.getObserveSafeWorkDistance());
			query.setParameter(6, jsaHazardsVoltageDto.getUseFlashBurn());
			query.setParameter(7, jsaHazardsVoltageDto.getUseInsulatedGloves());
			query.setParameter(8, tmpId);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public JsaHazardsVoltageDto getJsaHazardsVoltageDto(Integer id) {
		List<Object[]> obj;
		JsaHazardsVoltageDto jsaHazardsVoltageDto = new JsaHazardsVoltageDto();
		try {
			String sql = "select distinct PERMITNUMBER, HIGHVOLTAGE,RESTRICTACCESS,DISCHARGEEQUIPMENT, "
					+ " OBSERVESAFEWORKDISTANCE,USEFLASHBURN,USEINSULATEDGLOVES from IOP.TMPJSAHAZARDSVOLTAGE "
					+ " where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();

			for (Object[] a : obj) {
				jsaHazardsVoltageDto.setPermitNumber((Integer) a[0]);
				jsaHazardsVoltageDto.setHighVoltage(Integer.parseInt(a[1].toString()));
				jsaHazardsVoltageDto.setRestrictAccess(Integer.parseInt(a[2].toString()));
				jsaHazardsVoltageDto.setDischargeEquipment(Integer.parseInt(a[3].toString()));
				jsaHazardsVoltageDto.setObserveSafeWorkDistance(Integer.parseInt(a[4].toString()));
				jsaHazardsVoltageDto.setUseFlashBurn(Integer.parseInt(a[5].toString()));
				jsaHazardsVoltageDto.setUseInsulatedGloves(Integer.parseInt(a[6].toString()));
			}
			return jsaHazardsVoltageDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
