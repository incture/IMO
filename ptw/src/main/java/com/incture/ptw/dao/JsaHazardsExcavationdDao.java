package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsExcavationdDto;

@Repository
public class JsaHazardsExcavationdDao extends BaseDao {

	public void insertJsaHazardsExcavation(String permitNumber,JsaHazardsExcavationdDto jsaHazardsExcavationdDto) {
		try {
			logger.info("JsaHazardsExcavationdDto: " + jsaHazardsExcavationdDto);
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSEXCAVATION\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsExcavationdDto.getExcavations());
			query.setParameter(3, jsaHazardsExcavationdDto.getHaveExcavationPlan());
			query.setParameter(4, jsaHazardsExcavationdDto.getLocatePipeByHandsDigging());
			query.setParameter(5, jsaHazardsExcavationdDto.getDeEnergizeUnderground());
			query.setParameter(6, jsaHazardsExcavationdDto.getCsecontrols());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

}
