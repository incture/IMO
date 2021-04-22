package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsManualDto;

@Repository
public class JsaHazardsManualDao extends BaseDao {
	public void insertJsaHazardsManual(String permitNumber, JsaHazardsManualDto jsaHazardsManualDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMANUAL\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsManualDto.getManualHandling());
			query.setParameter(3, jsaHazardsManualDto.getAssessManualTask());
			query.setParameter(4, jsaHazardsManualDto.getLimitLoadSize());
			query.setParameter(5, jsaHazardsManualDto.getProperLiftingTechnique());
			query.setParameter(6, jsaHazardsManualDto.getConfirmStabilityOfLoad());
			query.setParameter(7, jsaHazardsManualDto.getGetAssistanceOrAid());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
