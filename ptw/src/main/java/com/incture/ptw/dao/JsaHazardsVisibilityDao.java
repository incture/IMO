package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsVisibilityDto;

@Repository
public class JsaHazardsVisibilityDao extends BaseDao {
	public void insertJsaHazardsVisibility(String permitNumber, JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		try{
			String sql="INSERT INTO \"IOP\".\"JSAHAZARDSVISIBILITY\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsVisibilityDto.getPoorLighting());
			query.setParameter(3, jsaHazardsVisibilityDto.getProvideAlternateLighting());
			query.setParameter(4, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
			query.setParameter(5, jsaHazardsVisibilityDto.getDeferUntilVisibilityImporve());
			query.setParameter(6, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
			query.executeUpdate();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

}
