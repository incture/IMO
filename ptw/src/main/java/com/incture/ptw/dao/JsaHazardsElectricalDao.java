package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsElectricalDto;

@Repository
public class JsaHazardsElectricalDao extends BaseDao {
	public void insertJsaHazardsElectrical(String permitNumber, JsaHazardsElectricalDto jsaHazardsElectricalDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSELECTRICAL\" VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsElectricalDto.getPortableElectricalEquipment());
			query.setParameter(3, jsaHazardsElectricalDto.getInspectToolsForCondition());
			query.setParameter(4, jsaHazardsElectricalDto.getImplementGasTesting());
			query.setParameter(5, jsaHazardsElectricalDto.getProtectElectricalLeads());
			query.setParameter(6, jsaHazardsElectricalDto.getIdentifyEquipClassification());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
