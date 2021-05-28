package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsPressurizedDto;

@Repository
public class JsaHazardsPressurizedDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsPressurized(String permitNumber, JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"JSAHAZARDSPRESSURIZED\" VALUES (?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaHazardsPressurizedDto.getPresurizedEquipment());
			query.setParameter(3, jsaHazardsPressurizedDto.getPerformIsolation());
			query.setParameter(4, jsaHazardsPressurizedDto.getDepressurizeDrain());
			query.setParameter(5, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
			query.setParameter(6, jsaHazardsPressurizedDto.getDoNotWorkInLineOfFire());
			query.setParameter(7, jsaHazardsPressurizedDto.getAnticipateResidual());
			query.setParameter(8, jsaHazardsPressurizedDto.getSecureAllHoses());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void updateJsaHazardsPressurized(JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		try {
			String sql = "UPDATE \"IOP\".\"JSAHAZARDSPRESSURIZED\" SET  \"PRESURIZEDEQUIPMENT\"=?,\"PERFORMISOLATION\"=?,\"DEPRESSURIZEDRAIN\"=?,"
					+ "\"RELIEVETRAPPEDPRESSURE\"=?,\"DONOTWORKINLINEOFFIRE\"=?,\"ANTICIPATERESIDUAL\"=?,\"SECUREALLHOSES\"=? WHERE \"PERMITNUMBER\"=?";
			logger.info("updateJsaHazardsPressurized sql" + sql);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, jsaHazardsPressurizedDto.getPresurizedEquipment());
			query.setParameter(2, jsaHazardsPressurizedDto.getPerformIsolation());
			query.setParameter(3, jsaHazardsPressurizedDto.getDepressurizeDrain());
			query.setParameter(4, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
			query.setParameter(5, jsaHazardsPressurizedDto.getDoNotWorkInLineOfFire());
			query.setParameter(6, jsaHazardsPressurizedDto.getAnticipateResidual());
			query.setParameter(7, jsaHazardsPressurizedDto.getSecureAllHoses());
			query.setParameter(8, jsaHazardsPressurizedDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public JsaHazardsPressurizedDto getjsaHazardsPress(String permitNum) {
		JsaHazardsPressurizedDto jsaHazardsPressurizedDto = new JsaHazardsPressurizedDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, PRESURIZEDEQUIPMENT,PERFORMISOLATION,DEPRESSURIZEDRAIN, "
					+ " RELIEVETRAPPEDPRESSURE,DONOTWORKINLINEOFFIRE,ANTICIPATERESIDUAL,SECUREALLHOSES "
					+ " from IOP.JSAHAZARDSPRESSURIZED where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPRESSURIZED sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaHazardsPressurizedDto.setPermitNumber((Integer) a[0]);
				jsaHazardsPressurizedDto.setPresurizedEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsPressurizedDto.setPerformIsolation(Integer.parseInt(a[2].toString()));
				jsaHazardsPressurizedDto.setDepressurizeDrain(Integer.parseInt(a[3].toString()));
				jsaHazardsPressurizedDto.setRelieveTrappedPressure(Integer.parseInt(a[4].toString()));
				jsaHazardsPressurizedDto.setDoNotWorkInLineOfFire(Integer.parseInt(a[5].toString()));
				jsaHazardsPressurizedDto.setAnticipateResidual(Integer.parseInt(a[6].toString()));
				jsaHazardsPressurizedDto.setSecureAllHoses(Integer.parseInt(a[7].toString()));
			}

			return jsaHazardsPressurizedDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
