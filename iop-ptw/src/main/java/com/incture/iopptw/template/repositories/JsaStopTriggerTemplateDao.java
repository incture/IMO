package com.incture.iopptw.template.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaStopTriggerTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaStopTrigger(String tmpId, JsaStopTriggerDto JsaStopTriggerDto) {
		try {
			logger.info("JsaStopTriggerDto: " + JsaStopTriggerDto);
			String sql = "INSERT INTO \"IOP\".\"TMPJSASTOPTRIGGER\" VALUES (?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, tmpId);
			query.setParameter(2, tmpId);
			query.setParameter(3, JsaStopTriggerDto.getLineDescription());
			query.setParameter(4, tmpId);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public List<JsaStopTriggerDto> getJsaStopTriggerDto(Integer id) {
		List<Object[]> obj;
		List<JsaStopTriggerDto> jsaStopTriggerDtoList = new ArrayList<JsaStopTriggerDto>();
		try {
			String sql = "select  SERIALNO,PERMITNUMBER, LINEDESCRIPTION from IOP.TMPJSASTOPTRIGGER"
					+ " where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				JsaStopTriggerDto jsaStopTriggerDto = new JsaStopTriggerDto();
				jsaStopTriggerDto.setSerialNo((Integer) a[0]);
				jsaStopTriggerDto.setPermitNumber((Integer) a[1]);
				jsaStopTriggerDto.setLineDescription((String) a[2]);
				jsaStopTriggerDtoList.add(jsaStopTriggerDto);
			}
			return jsaStopTriggerDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
