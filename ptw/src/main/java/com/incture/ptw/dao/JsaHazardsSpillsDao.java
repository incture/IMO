package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSpillsDto;

@Repository
public class JsaHazardsSpillsDao extends BaseDao{
	public void insertJsaHazardsSpills(String permitNumber,JsaHazardsSpillsDto jsaHazardsSpillsDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSSPILLS VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsSpillsDto.getPotentialSpills());
			query.setParameter(3, jsaHazardsSpillsDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSpillsDto.getConnectionsInGoodCondition());
			query.setParameter(5, jsaHazardsSpillsDto.getSpillContainmentEquipment());
			query.setParameter(6, jsaHazardsSpillsDto.getHaveSpillCleanupMaterials());
			query.setParameter(7, jsaHazardsSpillsDto.getRestrainHosesWhenNotInUse());
			query.executeUpdate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsSpillsDto getJsaHazardsSpillsDto(String permitNum){
		JsaHazardsSpillsDto jsaHazardsSpillsDto = new JsaHazardsSpillsDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, POTENTIALSPILLS,DRAINEQUIPMENT,CONNECTIONSINGOODCONDITION, "
					+ " SPILLCONTAINMENTEQUIPMENT,HAVESPILLCLEANUPMATERIALS,RESTRAINHOSESWHENNOTINUSE "
					+ " from IOP.JSAHAZARDSSPILLS where PERMITNUMBER = :permitNum";

			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
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

	public void updateJsaHazardsSpills(JsaHazardsSpillsDto jsaHazardsSpillsDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSSPILLS\" SET  \"POTENTIALSPILLS\"=?,\"DRAINEQUIPMENT\"=?,\"CONNECTIONSINGOODCONDITION\"=?," +
        "\"SPILLCONTAINMENTEQUIPMENT\"=?,\"HAVESPILLCLEANUPMATERIALS\"=?,\"RESTRAINHOSESWHENNOTINUSE\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsSpills sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsSpillsDto.getPotentialSpills());
			query.setParameter(2, jsaHazardsSpillsDto.getDrainEquipment());
			query.setParameter(3, jsaHazardsSpillsDto.getConnectionsInGoodCondition());
			query.setParameter(4, jsaHazardsSpillsDto.getSpillContainmentEquipment());
			query.setParameter(5, jsaHazardsSpillsDto.getHaveSpillCleanupMaterials());
			query.setParameter(6, jsaHazardsSpillsDto.getRestrainHosesWhenNotInUse());
			query.setParameter(7, jsaHazardsSpillsDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
