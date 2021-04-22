package com.incture.ptw.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsHeightsDto;

@Repository
public class JsaHazardsHeightsDao extends BaseDao {
	public void insertJsaHazardsHeights(String permitNumber, JsaHazardsHeightsDto jsaHazardsHeightsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSHEIGHTS\" VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsHeightsDto.getWorkAtHeights());
			query.setParameter(3, jsaHazardsHeightsDto.getDiscussWorkingPractice());
			query.setParameter(4, jsaHazardsHeightsDto.getVerifyFallRestraint());
			query.setParameter(5, jsaHazardsHeightsDto.getUsefullbodyHarness());
			query.setParameter(6, jsaHazardsHeightsDto.getUseLockTypeSnapHooks());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
