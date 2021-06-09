package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsWeatherDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsWeatherTemplateDao extends BaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	public void insertJsaHazardsWeatherTemplate(String id,JsaHazardsWeatherDto jsaHazardsWeatherDto){
		try{
			String sql = "INSERT INTO IOP.TMPJSAHAZARDSWEATHER VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsWeatherDto.getWeather());
			query.setParameter(3, jsaHazardsWeatherDto.getControlsForSlipperySurface());
			query.setParameter(4, jsaHazardsWeatherDto.getHeatBreak());
			query.setParameter(5, jsaHazardsWeatherDto.getColdHeaters());
			query.setParameter(6, jsaHazardsWeatherDto.getLightning());
			query.setParameter(7, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsWeatherDto getJsaHazardsWeatherTemplate(String id){
		List<Object[]> obj;
		JsaHazardsWeatherDto jsaHazardsWeatherDto = new JsaHazardsWeatherDto();
		try{
			String sql = "select distinct PERMITNUMBER, WEATHER,CONTROLSFORSLIPPERYSURFACE,HEATBREAK, "
					+ " COLDHEATERS,LIGHTNING from IOP.TMPJSAHAZARDSWEATHER where ID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
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
	

}
