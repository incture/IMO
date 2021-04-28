package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsToolsDto;

@Repository
public class JsaHazardsToolsDao extends BaseDao {
	public void insertJsaHazardsTools(String permitNumber, JsaHazardsToolsDto jsaHazardsToolsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSTOOLS\" VALUES (?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsToolsDto.getEquipmentAndTools());
			query.setParameter(3, jsaHazardsToolsDto.getInspectEquipmentTool());
			query.setParameter(4, jsaHazardsToolsDto.getBrassToolsNecessary());
			query.setParameter(5, jsaHazardsToolsDto.getUseProtectiveGuards());
			query.setParameter(6, jsaHazardsToolsDto.getUseCorrectTools());
			query.setParameter(7, jsaHazardsToolsDto.getCheckForSharpEdges());
			query.setParameter(8, jsaHazardsToolsDto.getApplyHandSafetyPrinciple());
			query.executeUpdate();
			logger.info("jsaHazardsToolsDto"+jsaHazardsToolsDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
