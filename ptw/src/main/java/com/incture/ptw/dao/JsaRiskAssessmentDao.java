package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaRiskAssesmentDto;

@Repository
public class JsaRiskAssessmentDao extends BaseDao {

	public void insertJsaRiskAssessment(String permitNumber, JsaRiskAssesmentDto jsaRiskAssesmentDto) {
		try {
			String sql = "INSERT INTO IOP.JSARISKASSESMENT VALUES (?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, jsaRiskAssesmentDto.getMustModifyExistingWorkPractice());
			query.setParameter(3, jsaRiskAssesmentDto.getHasContinuedRisk());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void updateJsaRiskAssessment(JsaRiskAssesmentDto jsaRiskAssesmentDto) {
		try {
			String sql = "UPDATE IOP.JSARISKASSESMENT SET MUSTMODIFYEXISTINGWORKPRACTICE=?,HASCONTINUEDRISK=? WHERE PERMITNUMBER=?";
			logger.info("updateJsaRiskAssessment sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaRiskAssesmentDto.getMustModifyExistingWorkPractice());
			query.setParameter(2, jsaRiskAssesmentDto.getHasContinuedRisk());
			query.setParameter(3, jsaRiskAssesmentDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
