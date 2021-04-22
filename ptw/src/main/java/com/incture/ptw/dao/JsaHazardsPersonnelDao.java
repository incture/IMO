package com.incture.ptw.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsPersonnelDto;

@Repository
public class JsaHazardsPersonnelDao extends BaseDao {

	public void insertJsaHazardsPersonnel(String permitNumber, JsaHazardsPersonnelDto jsaHazardsPersonnelDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSPERSONNEL VALUES (?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsPersonnelDto.getPersonnel());
			query.setParameter(3, jsaHazardsPersonnelDto.getPerformInduction());
			query.setParameter(4, jsaHazardsPersonnelDto.getMentorCoachSupervise());
			query.setParameter(5, jsaHazardsPersonnelDto.getVerifyCompetencies());
			query.setParameter(6, jsaHazardsPersonnelDto.getAddressLimitations());
			query.setParameter(7, jsaHazardsPersonnelDto.getManageLanguageBarriers());
			query.setParameter(8, jsaHazardsPersonnelDto.getWearSeatBelts());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
