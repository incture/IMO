package com.incture.iopptw.template.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.repositories.BaseDao;

@Repository
public class JsaStepsTemplateDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertJsaStepsTemplate(String id, JsaStepsDto jsaStepsDto) {
		try {
			logger.info("JsaStepsDto: " + jsaStepsDto);
			String sql = "INSERT INTO \"IOP\".\"TMPJSASTEPS\" VALUES (?,?,?,?,?,?,?)";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, id);
			query.setParameter(2, id);
			query.setParameter(3, jsaStepsDto.getTaskSteps());
			query.setParameter(4, jsaStepsDto.getPotentialHazards());
			query.setParameter(5, jsaStepsDto.getHazardControls());
			query.setParameter(6, jsaStepsDto.getPersonResponsible());
			query.setParameter(7, id);
			logger.info("sql " + sql);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}
	
	@SuppressWarnings("unchecked")
	public List<JsaStepsDto> getJsaStepsDto(Integer id) {
		List<Object[]> obj;
		List<JsaStepsDto> jsaStepsDtoList = new ArrayList<JsaStepsDto>();
		
		try {
			String sql = "select  SERIALNO,PERMITNUMBER, TASKSTEPS,POTENTIALHAZARDS,HAZARDCONTROLS,PERSONRESPONSIBLE "
					+ " from IOP.TMPJSASTEPS where TMPID = :id";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				JsaStepsDto jsaStepsDto = new JsaStepsDto();
				jsaStepsDto.setSerialNo((Integer) a[0]);
				jsaStepsDto.setPermitNumber((Integer) a[1]);
				jsaStepsDto.setTaskSteps((String) a[2]);
				jsaStepsDto.setPotentialHazards((String) a[3]);
				jsaStepsDto.setHazardControls((String) a[4]);
				jsaStepsDto.setPersonResponsible((String) a[5]);
				jsaStepsDtoList.add(jsaStepsDto);
			}
//			System.out.println(jsaStepsDto);
			return jsaStepsDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
