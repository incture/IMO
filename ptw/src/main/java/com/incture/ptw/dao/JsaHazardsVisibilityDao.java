package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsVisibilityDto;

@Repository
public class JsaHazardsVisibilityDao extends BaseDao {
	public void insertJsaHazardsVisibility(String permitNumber, JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		Query query = getSession()
				.createNativeQuery("INSERT INTO \"IOP\".\"JSAHAZARDSVISIBILITY\" VALUES (?,?,?,?,?,?)");
		query.setParameter(1, permitNumber);
		query.setParameter(2, jsaHazardsVisibilityDto.getPoorLighting());
		query.setParameter(3, jsaHazardsVisibilityDto.getAlternateLighting());
		query.setParameter(4, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
		query.setParameter(5, jsaHazardsVisibilityDto.getDeferUntilVisibility());
		query.setParameter(6, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
		query.executeUpdate();
	}

}
