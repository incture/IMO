package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsVoltageDto;

@Repository
public class JsaHazardsVoltageDao extends BaseDao {

	public void insertJsaHazardsVoltage(String permitNumber, JsaHazardsVoltageDto jsaHazardsVoltageDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSVOLTAGE\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsVoltageDto.getHighVoltage());
			query.setParameter(3, jsaHazardsVoltageDto.getRestrictAccess());
			query.setParameter(4, jsaHazardsVoltageDto.getDischargeEquipment());
			query.setParameter(5, jsaHazardsVoltageDto.getObserveSafeWorkDistance());
			query.setParameter(6, jsaHazardsVoltageDto.getUseFlashBurn());
			query.setParameter(7, jsaHazardsVoltageDto.getUseInsulatedGloves());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
