package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsSubstancesDto;

@Repository
public class JsaHazardsSubstancesDao extends BaseDao {

	public void insertJsaHazardsSubstances(String permitNumber, JsaHazardsSubstancesDto jsaHazardsSubstancesDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSSUBSTANCES VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsSubstancesDto.getHazardousSubstances());
			query.setParameter(3, jsaHazardsSubstancesDto.getDrainEquipment());
			query.setParameter(4, jsaHazardsSubstancesDto.getFollowSdsControls());
			query.setParameter(5, jsaHazardsSubstancesDto.getImplementHealthHazardControls());
			query.setParameter(6, jsaHazardsSubstancesDto.getTestMaterial());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
