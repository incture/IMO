package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsSpillsDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsSpillsTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsSpillsTemplate(String id, JsaHazardsSpillsDto jsaHazardsSpillsDto) {
		try {
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSSPILLS VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsSpillsDto.getPotentialSpills());
			query.setParameter(3, jsaHazardsSpillsDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSpillsDto.getConnectionsInGoodCondition());
			query.setParameter(5, jsaHazardsSpillsDto.getSpillContainmentEquipment());
			query.setParameter(6, jsaHazardsSpillsDto.getHaveSpillCleanupMaterials());
			query.setParameter(7, jsaHazardsSpillsDto.getRestrainHosesWhenNotInUse());
			query.setParameter(8, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsSpillsDto getJsaHazardsSpillsDto(Integer id){
		JsaHazardsSpillsDto jsaHazardsSpillsDto = new JsaHazardsSpillsDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, POTENTIALSPILLS,DRAINEQUIPMENT,CONNECTIONSINGOODCONDITION, "
					+ " SPILLCONTAINMENTEQUIPMENT,HAVESPILLCLEANUPMATERIALS,RESTRAINHOSESWHENNOTINUSE "
					+ " from IOP.TMPJSAHAZARDSSPILLS where TMPID = :id";

			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsSpillsDto.setPermitNumber((Integer) a[0]);
				jsaHazardsSpillsDto.setPotentialSpills(Integer.parseInt(a[1].toString()));
				jsaHazardsSpillsDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
				jsaHazardsSpillsDto.setConnectionsInGoodCondition(Integer.parseInt(a[3].toString()));
				jsaHazardsSpillsDto.setSpillContainmentEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsSpillsDto.setHaveSpillCleanupMaterials(Integer.parseInt(a[5].toString()));
				jsaHazardsSpillsDto.setRestrainHosesWhenNotInUse(Integer.parseInt(a[6].toString()));

			}
			return jsaHazardsSpillsDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
