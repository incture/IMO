package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardscseDto;

@Repository
public class JsaHazardsCseDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsCse(String permitNumber, JsaHazardscseDto jsaHazardscseDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHAZARDSCSE VALUES (?,?,?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
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
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardscseDto getJsaHazardsCse(String permitNum) {
		List<Object[]> obj;
		JsaHazardscseDto jsaHazardscseDto = new JsaHazardscseDto();
		try {
			String sql = "select distinct PERMITNUMBER, CONFINEDSPACEENTRY,DISCUSSWORKPRACTICE, "
					+ " CONDUCTATMOSPHERICTESTING,MONITORACCESS,PROTECTSURFACES,PROHIBITMOBILEENGINE, "
					+ " PROVIDEOBSERVER,DEVELOPRESCUEPLAN from IOP.JSAHAZARDSCSE where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSCSE sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardscseDto.setPermitNumber((Integer) a[0]);
				jsaHazardscseDto.setConfinedSpaceEntry(Integer.parseInt(a[1].toString()));
				jsaHazardscseDto.setDiscussWorkPractice(Integer.parseInt(a[2].toString()));
				jsaHazardscseDto.setConductAtmosphericTesting(Integer.parseInt(a[3].toString()));
				jsaHazardscseDto.setMonitorAccess(Integer.parseInt(a[4].toString()));
				jsaHazardscseDto.setProtectSurfaces(Integer.parseInt(a[5].toString()));
				jsaHazardscseDto.setProhibitMobileEngine(Integer.parseInt(a[6].toString()));
				jsaHazardscseDto.setProvideObserver(Integer.parseInt(a[7].toString()));
				jsaHazardscseDto.setDevelopRescuePlan(Integer.parseInt(a[8].toString()));
			}
			return jsaHazardscseDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsCse(JsaHazardscseDto jsaHazardscseDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSCSE\" SET  \"CONFINEDSPACEENTRY\"=?,\"DISCUSSWORKPRACTICE\"=?,\"CONDUCTATMOSPHERICTESTING\"=?,"
					+ "\"MONITORACCESS\"=?,\"PROTECTSURFACES\"=?,\"PROHIBITMOBILEENGINE\"=?,\"PROVIDEOBSERVER\"=?,\"DEVELOPRESCUEPLAN\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsCse sql" + sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsaHazardscseDto.getConfinedSpaceEntry());
			query.setParameter(2, jsaHazardscseDto.getDiscussWorkPractice());
			query.setParameter(3, jsaHazardscseDto.getConductAtmosphericTesting());
			query.setParameter(4, jsaHazardscseDto.getMonitorAccess());
			query.setParameter(5, jsaHazardscseDto.getProtectSurfaces());
			query.setParameter(6, jsaHazardscseDto.getProhibitMobileEngine());
			query.setParameter(7, jsaHazardscseDto.getProvideObserver());
			query.setParameter(8, jsaHazardscseDto.getDevelopRescuePlan());
			query.setParameter(9, jsaHazardscseDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
}
