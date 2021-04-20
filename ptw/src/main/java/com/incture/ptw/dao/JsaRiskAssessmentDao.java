package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaRiskAssesmentDto;

@Repository
public class JsaRiskAssessmentDao extends BaseDao {
	public void insertJsaRiskAssessment(JsaRiskAssesmentDto jsaRiskAssesmentDto) {
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"JSARISKASSESMENT\" VALUES (?,?,?)");
		query.setParameter(1, jsaRiskAssesmentDto.getPermitNumber());
		query.setParameter(2, jsaRiskAssesmentDto.getMustModifyExistingWorkPractice());
		query.setParameter(3, jsaRiskAssesmentDto.getHasContinuedRisk());
		query.executeUpdate();
	}

}
