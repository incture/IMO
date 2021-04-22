package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsFallsDto;

@Repository
public class JsaHazardsFallsDao extends BaseDao {

	public void insertJsaHazardsFalls(String permitNumber, JsaHazardsFallsDto jsaHazardsFallsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSFALLS\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsFallsDto.getSlipstripsAndFalls());
			query.setParameter(3, jsaHazardsFallsDto.getIdentifyProjections());
			query.setParameter(4, jsaHazardsFallsDto.getFlagHazards());
			query.setParameter(5, jsaHazardsFallsDto.getSecureCables());
			query.setParameter(6, jsaHazardsFallsDto.getCleanupLiquids());
			query.setParameter(7, jsaHazardsFallsDto.getBarricadeHoles());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
