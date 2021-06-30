package com.incture.iopptw.template.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaRiskAssesmentDto;
import com.incture.iopptw.repositories.BaseDao;
@Repository
public class JsaRiskAssesmentTemplateDao extends BaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void insertJsaRiskAssesment(String tmpId,JsaRiskAssesmentDto jsaRiskAssesmentDto){
		try {
			logger.info("JsaRiskAssesmentDto: " + jsaRiskAssesmentDto);
			String sql = "INSERT INTO IOP.TMPJSARISKASSESMENT VALUES (?,?,?,?)";
			Session session= sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, tmpId);
			query.setParameter(2, jsaRiskAssesmentDto.getMustModifyExistingWorkPractice());
			query.setParameter(3, jsaRiskAssesmentDto.getHasContinuedRisk());
			query.setParameter(4, tmpId);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaRiskAssesmentDto getJsaRiskAss(Integer id){
		JsaRiskAssesmentDto jsaRiskAssesmentDto = new JsaRiskAssesmentDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, MUSTMODIFYEXISTINGWORKPRACTICE,HASCONTINUEDRISK "
					+ " from IOP.TMPJSARISKASSESMENT where TMPID = :id";
			logger.info("JSARISKASSESMENT sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("id", id);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaRiskAssesmentDto.setPermitNumber((Integer) a[0]);
				jsaRiskAssesmentDto.setMustModifyExistingWorkPractice(Integer.parseInt(a[1].toString()));
				jsaRiskAssesmentDto.setHasContinuedRisk(Integer.parseInt(a[2].toString()));
			}
			return jsaRiskAssesmentDto;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
