package com.incture.ptw.dao;

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

}
