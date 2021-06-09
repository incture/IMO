package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsElectricalDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsElectricalTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsElectrical(String id, JsaHazardsElectricalDto jsaHazardsElectricalDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSELECTRICAL\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsElectricalDto.getPortableElectricalEquipment());
			query.setParameter(3, jsaHazardsElectricalDto.getInspectToolsForCondition());
			query.setParameter(4, jsaHazardsElectricalDto.getImplementGasTesting());
			query.setParameter(5, jsaHazardsElectricalDto.getProtectElectricalLeads());
			query.setParameter(6, jsaHazardsElectricalDto.getIdentifyEquipClassification());
			query.setParameter(7, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsElectricalDto getJsaHazardsElectricalDto(Integer id) {
		JsaHazardsElectricalDto jsaHazardsElectricalDto = new JsaHazardsElectricalDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, PORTABLEELECTRICALEQUIPMENT,INSPECTTOOLSFORCONDITION, "
					+ " IMPLEMENTGASTESTING,PROTECTELECTRICALLEADS,IDENTIFYEQUIPCLASSIFICATION "
					+ " from IOP.TMPJSAHAZARDSELECTRICAL where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsElectricalDto.setPermitNumber((Integer) a[0]);
				jsaHazardsElectricalDto.setPortableElectricalEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsElectricalDto.setInspectToolsForCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsElectricalDto.setImplementGasTesting(Integer.parseInt(a[3].toString()));
				jsaHazardsElectricalDto.setProtectElectricalLeads(Integer.parseInt(a[4].toString()));
				jsaHazardsElectricalDto.setIdentifyEquipClassification(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsElectricalDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
