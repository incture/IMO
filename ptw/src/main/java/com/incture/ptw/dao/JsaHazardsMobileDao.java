package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsMobileDto;

@Repository
public class JsaHazardsMobileDao extends BaseDao {

	public void insertJsaHazardsMobile(String permitNumber,JsaHazardsMobileDto jsaHazardsMobileDto) {
		try {
			logger.info("JsaHazardsMobileDto: " + jsaHazardsMobileDto);
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSMOBILE\" VALUES (?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsMobileDto.getMobileEquipment());
			query.setParameter(3, jsaHazardsMobileDto.getAssessEquipmentCondition());
			query.setParameter(4, jsaHazardsMobileDto.getControlAccess());
			query.setParameter(5, jsaHazardsMobileDto.getMonitorProximity());
			query.setParameter(6, jsaHazardsMobileDto.getManageOverheadHazards());
			query.setParameter(7, jsaHazardsMobileDto.getAdhereToRules());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

}
