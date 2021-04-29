package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaHazardsWeatherDto;

@Repository
public class JsaHazardsWeatherDao extends BaseDao{
	public void insertJsaHazardsWeather(String permitNumber,JsaHazardsWeatherDto jsaHazardsWeatherDto){
		try{
			String sql = "INSERT INTO IOP.JSAHAZARDSWEATHER VALUES (?,?,?,?,?,?)";
			logger.info(sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsWeatherDto.getWeather());
			query.setParameter(3, jsaHazardsWeatherDto.getControlsForSlipperySurface());
			query.setParameter(4, jsaHazardsWeatherDto.getHeatBreak());
			query.setParameter(5, jsaHazardsWeatherDto.getColdHeaters());
			query.setParameter(6, jsaHazardsWeatherDto.getLightning());
			query.executeUpdate();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsWeatherDto getJsaHazardsWeatherDto(String permitNum){
		List<Object[]> obj;
		JsaHazardsWeatherDto jsaHazardsWeatherDto = new JsaHazardsWeatherDto();
		try{
			String sql = "select distinct PERMITNUMBER, WEATHER,CONTROLSFORSLIPPERYSURFACE,HEATBREAK, "
					+ " COLDHEATERS,LIGHTNING from IOP.JSAHAZARDSWEATHER where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsWeatherDto.setPermitNumber((Integer) a[0]);
				jsaHazardsWeatherDto.setWeather(Integer.parseInt(a[1].toString()));
				jsaHazardsWeatherDto.setControlsForSlipperySurface(Integer.parseInt(a[2].toString()));
				jsaHazardsWeatherDto.setHeatBreak(Integer.parseInt(a[3].toString()));
				jsaHazardsWeatherDto.setColdHeaters(Integer.parseInt(a[4].toString()));
				jsaHazardsWeatherDto.setLightning(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsWeatherDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsWeather(JsaHazardsWeatherDto jsaHazardsWeatherDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSWEATHER\" SET  \"WEATHER\"=?,\"CONTROLSFORSLIPPERYSURFACE\"=?,\"HEATBREAK\"=?," +
        "\"COLDHEATERS\"=?,\"LIGHTNING\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsWeather sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaHazardsWeatherDto.getWeather());
			query.setParameter(2, jsaHazardsWeatherDto.getControlsForSlipperySurface());
			query.setParameter(3, jsaHazardsWeatherDto.getHeatBreak());
			query.setParameter(4, jsaHazardsWeatherDto.getColdHeaters());
			query.setParameter(5, jsaHazardsWeatherDto.getLightning());
			query.setParameter(6, jsaHazardsWeatherDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
