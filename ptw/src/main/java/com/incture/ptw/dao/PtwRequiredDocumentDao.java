package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwRequiredDocumentDto;

@Repository
public class PtwRequiredDocumentDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;
	public void insertPtwRequiredDocument(String permitNumber, PtwRequiredDocumentDto ptwRequiredDocumentDto) {
		try {
			Query query = getSession().createNativeQuery(
					"INSERT INTO \"IOP\".\"PTWREQUIREDDOCUMENT\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, Integer.parseInt(keyGeneratorDao.getPTWREQDOC()));
			query.setParameter(2, Integer.parseInt(permitNumber));
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public List<PtwRequiredDocumentDto> getPtwReqDoc(String permitNumber, String isCwp, String isHwp, String isCse) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
