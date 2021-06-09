package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaHazardsPressurizedDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaHazardsPressurizedTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaHazardsPressurizedTemplate(String id, JsaHazardsPressurizedDto jsaHazardsPressurizedDto) {
		try {
			String sql = "INSERT INTO \"IOP\".\"TMPJSAHAZARDSPRESSURIZED\" VALUES (?,?,?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, null);
			query.setParameter(2, jsaHazardsPressurizedDto.getPresurizedEquipment());
			query.setParameter(3, jsaHazardsPressurizedDto.getPerformIsolation());
			query.setParameter(4, jsaHazardsPressurizedDto.getDepressurizeDrain());
			query.setParameter(5, jsaHazardsPressurizedDto.getRelieveTrappedPressure());
			query.setParameter(6, jsaHazardsPressurizedDto.getDoNotWorkInLineOfFire());
			query.setParameter(7, jsaHazardsPressurizedDto.getAnticipateResidual());
			query.setParameter(8, jsaHazardsPressurizedDto.getSecureAllHoses());
			query.setParameter(9, id);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaHazardsPressurizedDto getjsaHazardsPress(Integer id) {
		JsaHazardsPressurizedDto jsaHazardsPressurizedDto = new JsaHazardsPressurizedDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, PRESURIZEDEQUIPMENT,PERFORMISOLATION,DEPRESSURIZEDRAIN, "
					+ " RELIEVETRAPPEDPRESSURE,DONOTWORKINLINEOFFIRE,ANTICIPATERESIDUAL,SECUREALLHOSES "
					+ " from IOP.TMPJSAHAZARDSPRESSURIZED where TMPID = :id";
			logger.info("JSAHAZARDSPRESSURIZED sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			System.out.println(obj.toArray());
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
			System.out.println(jsaHazardsPressurizedDto);
			return jsaHazardsPressurizedDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
