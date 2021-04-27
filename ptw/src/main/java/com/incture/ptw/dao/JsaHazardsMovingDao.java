package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaHazardsMovingDto;

@Repository
public class JsaHazardsMovingDao extends BaseDao {
	public void insertJsaHazardsMoving(String permitNumber, JsaHazardsMovingDto jsaHazardsMovingDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMOVING\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsMovingDto.getMovingEquipment());
			query.setParameter(3, jsaHazardsMovingDto.getConfirmMachineryIntegrity());
			query.setParameter(4, jsaHazardsMovingDto.getProvideProtectiveBarriers());
			query.setParameter(5, jsaHazardsMovingDto.getObserverToMonitorProximityPeopleAndEquipment());
			query.setParameter(6, jsaHazardsMovingDto.getLockOutEquipment());
			query.setParameter(7, jsaHazardsMovingDto.getDoNotWorkInLineOfFire());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
