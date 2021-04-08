package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwRequiredDocumentDto;

@Repository
public class PtwRequiredDocumentDao extends BaseDao{
	public void insertPtwRequiredDocument(PtwRequiredDocumentDto ptwRequiredDocumentDto){
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"PTWREQUIREDDOCUMENT\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		query.setParameter(1, ptwRequiredDocumentDto.getSerialNo());
		query.setParameter(2, ptwRequiredDocumentDto.getPermitNumber());
		query.setParameter(3, ptwRequiredDocumentDto.getIsCwp());
		query.setParameter(4, ptwRequiredDocumentDto.getIsHwp());
		query.setParameter(5, ptwRequiredDocumentDto.getIsCse());
		query.setParameter(6, ptwRequiredDocumentDto.getAtmosphericTestRecord());
		query.setParameter(7, ptwRequiredDocumentDto.getLoto());
		query.setParameter(8, ptwRequiredDocumentDto.getProcedure());
		query.setParameter(9, ptwRequiredDocumentDto.getPandidorDrwaing());
		query.setParameter(10, ptwRequiredDocumentDto.getCertificate());
		query.setParameter(11, ptwRequiredDocumentDto.getTemporaryDefeat());
		query.setParameter(12, ptwRequiredDocumentDto.getRescuePlan());
		query.setParameter(13, ptwRequiredDocumentDto.getSds());
		query.setParameter(14, ptwRequiredDocumentDto.getOtherWorkPermitDocs());
		query.setParameter(15, ptwRequiredDocumentDto.getFireWatchCheckList());
		query.setParameter(16, ptwRequiredDocumentDto.getLiftPlan());
		query.setParameter(17, ptwRequiredDocumentDto.getSimopDeviation());
		query.setParameter(18, ptwRequiredDocumentDto.getSafeWorkPractice());
		query.executeUpdate();	
	}

}
