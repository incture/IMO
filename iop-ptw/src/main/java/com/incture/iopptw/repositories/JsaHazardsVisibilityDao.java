package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsVisibilityDto;

@Repository
public class JsaHazardsVisibilityDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;
	public void insertJsaHazardsVisibility(String permitNumber, JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		try{
			String sql="INSERT INTO \"IOP\".\"JSAHAZARDSVISIBILITY\" VALUES (?,?,?,?,?,?)";
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsVisibilityDto.getPoorLighting());
			query.setParameter(3, jsaHazardsVisibilityDto.getProvideAlternateLighting());
			query.setParameter(4, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
			query.setParameter(5, jsaHazardsVisibilityDto.getDeferUntilVisibilityImprove());
			query.setParameter(6, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
			query.executeUpdate();
			tx.commit();
			session.close();
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsVisibilityDto getJsaHazardsVisible(String permitNum){
		JsaHazardsVisibilityDto jsaHazardsVisibilityDto = new JsaHazardsVisibilityDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, POORLIGHTING,PROVIDEALTERNATELIGHTING, "
					+ " WAITUNTILVISIBILITYIMPROVE,DEFERUNTILVISIBILITYIMPROVE,KNOWDISTANCEFROMPOLES "
					+ " from IOP.JSAHAZARDSVISIBILITY where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSVISIBILITY sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsVisibilityDto.setPermitNumber((Integer) a[0]);
				jsaHazardsVisibilityDto.setPoorLighting(Integer.parseInt(a[1].toString()));
				jsaHazardsVisibilityDto.setProvideAlternateLighting(Integer.parseInt(a[2].toString()));
				jsaHazardsVisibilityDto.setWaitUntilVisibilityImprove(Integer.parseInt(a[3].toString()));
				jsaHazardsVisibilityDto.setDeferUntilVisibilityImprove(Integer.parseInt(a[4].toString()));
				jsaHazardsVisibilityDto.setKnowDistanceFromPoles(Integer.parseInt(a[5].toString()));
			}
			return jsaHazardsVisibilityDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsVisibility(JsaHazardsVisibilityDto jsaHazardsVisibilityDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSVISIBILITY\" SET  \"POORLIGHTING\"=?,\"PROVIDEALTERNATELIGHTING\"=?,\"WAITUNTILVISIBILITYIMPROVE\"=?," +
        "\"DEFERUNTILVISIBILITYIMPROVE\"=?,\"KNOWDISTANCEFROMPOLES\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsVisibility sql" + sql);
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsaHazardsVisibilityDto.getPoorLighting());
			query.setParameter(2, jsaHazardsVisibilityDto.getProvideAlternateLighting());
			query.setParameter(3, jsaHazardsVisibilityDto.getWaitUntilVisibilityImprove());
			query.setParameter(4, jsaHazardsVisibilityDto.getDeferUntilVisibilityImprove());
			query.setParameter(5, jsaHazardsVisibilityDto.getKnowDistanceFromPoles());
			query.setParameter(6, jsaHazardsVisibilityDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
