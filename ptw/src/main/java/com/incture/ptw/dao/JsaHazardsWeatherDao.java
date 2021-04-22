package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsWeatherDto;

@Repository
public class JsaHazardsWeatherDao extends BaseDao{
	public void insertJsaHazardsWeatherDao(String permitNumber,JsaHazardsWeatherDto jsaHazardsWeatherDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSWEATHER VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsWeatherDto.getWeather());
			query.setParameter(3, jsaHazardsWeatherDto.getControlsForLipperySurface());
			query.setParameter(4, jsaHazardsWeatherDto.getHeatBreak());
			query.setParameter(5, jsaHazardsWeatherDto.getColdHeaters());
			query.setParameter(6, jsaHazardsWeatherDto.getLightning());
			query.executeUpdate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
}
