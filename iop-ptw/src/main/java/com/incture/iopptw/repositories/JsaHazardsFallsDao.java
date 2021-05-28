package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsFallsDto;

@Repository
public class JsaHazardsFallsDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsFalls(String permitNumber, JsaHazardsFallsDto jsaHazardsFallsDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSFALLS\" VALUES (?,?,?,?,?,?,?)";
			logger.info(sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsFallsDto.getSlipsTripsAndFalls());
			query.setParameter(3, jsaHazardsFallsDto.getIdentifyProjections());
			query.setParameter(4, jsaHazardsFallsDto.getFlagHazards());
			query.setParameter(5, jsaHazardsFallsDto.getSecureCables());
			query.setParameter(6, jsaHazardsFallsDto.getCleanUpLiquids());
			query.setParameter(7, jsaHazardsFallsDto.getBarricadeHoles());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public JsaHazardsFallsDto getJsaHazardsFallsDto(String permitNum) {
		List<Object[]> obj;
		JsaHazardsFallsDto jsaHazardsFallsDto = new JsaHazardsFallsDto();
		try {
			String sql = "select distinct PERMITNUMBER, SLIPSTRIPSANDFALLS,IDENTIFYPROJECTIONS,FLAGHAZARDS, "
					+ " SECURECABLES,CLEANUPLIQUIDS,BARRICADEHOLES from IOP.JSAHAZARDSFALLS "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsFallsDto.setPermitNumber((Integer) a[0]);
				jsaHazardsFallsDto.setSlipsTripsAndFalls(Integer.parseInt(a[1].toString()));
				jsaHazardsFallsDto.setIdentifyProjections(Integer.parseInt(a[2].toString()));
				jsaHazardsFallsDto.setFlagHazards(Integer.parseInt(a[3].toString()));
				jsaHazardsFallsDto.setSecureCables(Integer.parseInt(a[4].toString()));
				jsaHazardsFallsDto.setCleanUpLiquids(Integer.parseInt(a[5].toString()));
				jsaHazardsFallsDto.setBarricadeHoles(Integer.parseInt(a[6].toString()));

			}
			return jsaHazardsFallsDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updateJsaHazardsFalls(JsaHazardsFallsDto jsaHazardsFallsDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSFALLS\" SET  \"SLIPSTRIPSANDFALLS\"=?,\"IDENTIFYPROJECTIONS\"=?,\"FLAGHAZARDS\"=?,"
					+ "\"SECURECABLES\"=?,\"CLEANUPLIQUIDS\"=?,\"BARRICADEHOLES\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsFalls sql" + sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsaHazardsFallsDto.getSlipsTripsAndFalls());
			query.setParameter(2, jsaHazardsFallsDto.getIdentifyProjections());
			query.setParameter(3, jsaHazardsFallsDto.getFlagHazards());
			query.setParameter(4, jsaHazardsFallsDto.getSecureCables());
			query.setParameter(5, jsaHazardsFallsDto.getCleanUpLiquids());
			query.setParameter(6, jsaHazardsFallsDto.getBarricadeHoles());
			query.setParameter(7, jsaHazardsFallsDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
