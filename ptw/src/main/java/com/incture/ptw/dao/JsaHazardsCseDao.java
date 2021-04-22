package com.incture.ptw.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardscseDto;

@Repository
public class JsaHazardsCseDao extends BaseDao {
	

	public void insertJsaHazardsCse(String permitNumber,JsaHazardscseDto jsaHazardscseDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSCSE VALUES (?,?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardscseDto.getConfinedSpaceEntry());
			query.setParameter(3, jsaHazardscseDto.getDiscussWorkPractice());
			query.setParameter(4, jsaHazardscseDto.getConductAtmosphericTesting());
			query.setParameter(5, jsaHazardscseDto.getMonitorAccess());
			query.setParameter(6, jsaHazardscseDto.getProtectSurfaces());
			query.setParameter(7, jsaHazardscseDto.getProhibitMobileEngine());
			query.setParameter(8, jsaHazardscseDto.getProvideObserver());
			query.setParameter(9, jsaHazardscseDto.getDevelopRescuePlan());
			query.executeUpdate();
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
	}
}
