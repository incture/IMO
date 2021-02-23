package com.murphy.taskmgmt.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.LocationDistancesDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("LocationDistancesDao")
public class LocationDistancesDao{
	private static final Logger logger = LoggerFactory.getLogger(LocationDistancesDao.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	public LocationDistancesDao() {
	}


//	public Session getSession() {
//		/*
//		 * try { ServicesUtil.unSetupSOCKS(); logger.error("basedao"+System.getProperty("socksProxyHost"));
//		 * logger.error("basedao"+System.getProperty("socksProxyPort")); logger.error("basedao"+System.getProperty("java.net.socks.username"));
//		 * while(System.getProperty(MurphyConstant.SOCKS_PORT_NAME).equals(MurphyConstant.SOCKS_PORT)) { } } catch (Exception e) {
//		 * logger.error("[Murphy][BaseDao][getSession][Socks Exception] "+e.getMessage()); }
//		 */
//		try {
//			return sessionFactory.getCurrentSession();
//		} catch (HibernateException e) {
//			logger.error("[Murphy][BaseDao][getSession][error] " + e.getMessage());
//			return sessionFactory.openSession();
//		}
//
//	}
	@SuppressWarnings("unchecked")
	public LocationDistancesDto getDistance(String fromLocation, String toLoc) {
		LocationDistancesDto response = new LocationDistancesDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Distance Not Found");
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		Transaction tx = null;
		Session session = null;
		List<Object[]> result=null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String qry = "SELECT ROAD_DRIVE_TIME,ROAD_TOTAL_TIME,CROW_FLY_LENGTH FROM LOCATION_DISTANCES WHERE FROM_LOCATION_CODE='"
					+ fromLocation + "' AND TO_LOCATION_CODE='" + toLoc + "' UNION SELECT ROAD_DRIVE_TIME,ROAD_TOTAL_TIME,CROW_FLY_LENGTH FROM LOCATION_DISTANCES WHERE TO_LOCATION_CODE='"
						+ fromLocation + "' AND FROM_LOCATION_CODE='" + toLoc + "'";
			result = session.createSQLQuery(qry).list();
			if (!ServicesUtil.isEmpty(result)) {
				Object[] obj = result.get(0);
				Double time=0.0;
				if (obj[0].getClass().getName().equals("java.lang.Double")) {
					time = (Double) obj[0];
				} else if (obj[0].getClass().getName().equals("java.math.BigDecimal")) {
					time = ((BigDecimal) obj[0]).doubleValue();
				}
				else {
					logger.error("obj[0].getClass().getName()" + obj[0].getClass().getName());
				}
				response.setRoadDriveTime(time);
				time=0.0;
				if (obj[1].getClass().getName().equals("java.lang.Double")) {
					time = (Double) obj[1];
				} else if (obj[1].getClass().getName().equals("java.math.BigDecimal")) {
					time = ((BigDecimal) obj[1]).doubleValue();
				}
				else {
					logger.error("obj[1].getClass().getName()" + obj[1].getClass().getName());
				}
				response.setRoadTotalTime(time);
				time=0.0;
				if (obj[2].getClass().getName().equals("java.lang.Double")) {
					time = (Double) obj[2];
				} else if (obj[2].getClass().getName().equals("java.math.BigDecimal")) {
					time = ((BigDecimal) obj[2]).doubleValue();
				} 
				else {
					logger.error("obj[2].getClass().getName()" + obj[2].getClass().getName());
				}
				response.setCrowFlyLength(time);
				responseMessage.setMessage("distance fetched successfully");
				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error("[LocationDistancesDao][getDistance][Exception]"+e.getMessage());
		}
		finally {
			try {
				if(!ServicesUtil.isEmpty(session))
					{
					session.close();
					}
			} catch (Exception e) {
				logger.error("[LocationDistancesDao][getDistance][Exception] Exception While Closing Session " + e.getMessage());
			}
		}
		response.setResponseMessage(responseMessage);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUniqueWells(){
		List<String> locationCodes=new ArrayList<String>();
		Transaction tx = null;
		Session session = null;
		List<String> result=null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String qry = "SELECT DISTINCT FROM_LOCATION_CODE FROM LOCATION_DISTANCES";
			result = session.createSQLQuery(qry).list();
			if(!ServicesUtil.isEmpty(result)){
				for(String location:result){
					locationCodes.add(location);
				}
			}
			session.flush();
			session.clear();
		}catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			logger.error("[LocationDistancesDao][getUniqueWells][Exception]"+e.getMessage());
		}
		finally {
			try {
				if(!ServicesUtil.isEmpty(session))
					{
					session.close();
					}
			} catch (Exception e) {
				logger.error("[LocationDistancesDao][getUniqueWells][Exception] Exception While Closing Session " + e.getMessage());
			}
		}
		return locationCodes;
	}
	
	public List<String> insertWell(LocationDistancesDto dto) {
		List<String> locationCodes = new ArrayList<String>();
		Transaction tx = null;
		Session session = null;
		int result = 0;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			String qry = "INSERT INTO LOCATION_DISTANCES VALUES(" + "'" + dto.getFromLocCode() + "',"
					+ dto.getFromLatitude() + "," + dto.getFromLongitude() + ",'" + dto.getToLocCode() + "',"
					+ dto.getToLatitude() + "," + dto.getToLongitude() + "," + dto.getRoadDriveTime() + ","
					+ dto.getRoadTotalTime() + "," + dto.getRoadLength() + "," + dto.getCrowFlyLength() + ")";
			logger.error("[LocationDistancesDao][insertWell] query "+qry);
			result = session.createSQLQuery(qry).executeUpdate();
			if(result>0){
//				logger.error("[LocationDistancesDao][insertWell] distance inserted Succesfully from "+dto.getFromLocCode() +" to "+dto.getToLocCode());
			}
			else{
				logger.error("[LocationDistancesDao][insertWell] distance insertion failed for from "+dto.getFromLocCode() +" to "+dto.getToLocCode());
			}
			tx.commit();
			session.flush();
			session.clear();
		} catch (Exception e) {
			// TODO: handle exception
			tx.rollback();
			logger.error("[LocationDistancesDao][insertWell][Exception]" + e.getMessage());
		} finally {
			try {
				if (!ServicesUtil.isEmpty(session)) {
					session.close();
				}
			} catch (Exception e) {
				logger.error("[LocationDistancesDao][insertWell][Exception] Exception While Closing Session "
						+ e.getMessage());
			}
		}
		return locationCodes;
	}

}
