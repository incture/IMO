package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsVisibilityDto;

@Repository
public class JsaHazardsVisibilityDao extends BaseDao {
	public void insertJsaHazardsVisibility(JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		Query query = getSession()
				.createNativeQuery("INSERT INTO \"IOP\".\"JSAHAZARDSVISIBILITY\" VALUES (?,?,?,?,?,?)");
		query.setParameter(1, jsaHazardsVisibilityDto.getPermitNumber());
		query.setParameter(2, jsaHazardsVisibilityDto.getPoorLighting());
		query.setParameter(3, jsaHazardsVisibilityDto.getAlternateLighting());
		query.setParameter(4, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
		query.setParameter(5, jsaHazardsVisibilityDto.getDeferUntilVisibility());
		query.setParameter(6, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
		query.executeUpdate();
	}

}
