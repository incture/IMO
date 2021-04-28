package com.incture.ptw.dao;

import java.util.ArrayList;
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
			logger.info("ptwRequiredDocumentDto ": ptwRequiredDocumentDto);
			Query query = getSession().createNativeQuery(
					"INSERT INTO \"IOP\".\"PTWREQUIREDDOCUMENT\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, Integer.parseInt(keyGeneratorDao.getPTWREQDOC()));
			query.setParameter(2, Integer.parseInt(permitNumber));
			query.setParameter(3, ptwRequiredDocumentDto.getIsCWP());
			query.setParameter(4, ptwRequiredDocumentDto.getIsHWP());
			query.setParameter(5, ptwRequiredDocumentDto.getIsCSE());
			query.setParameter(6, ptwRequiredDocumentDto.getAtmosphericTestRecord());
			query.setParameter(7, ptwRequiredDocumentDto.getLoto());
			query.setParameter(8, ptwRequiredDocumentDto.getProcedure());
			query.setParameter(9, ptwRequiredDocumentDto.getPAndIdOrDrawing());
			query.setParameter(10, ptwRequiredDocumentDto.getCertificate());
			query.setParameter(11, ptwRequiredDocumentDto.getTemporaryDefeat());
			query.setParameter(12, ptwRequiredDocumentDto.getRescuePlan());
			query.setParameter(13, ptwRequiredDocumentDto.getSds());
			query.setParameter(14, ptwRequiredDocumentDto.getOtherWorkPermitDocs());
			query.setParameter(15, ptwRequiredDocumentDto.getFireWatchChecklist());
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
			String sql = "select * from IOP.PTWREQUIREDDOCUMENT where PERMITNUMBER=? and ISCWP = ? and ISHWP = ? and ISCSE = ?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, isCwp);
			query.setParameter(3, isHwp);
			query.setParameter(4, isCse);
			logger.info("getPtwReqDoc sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> list = query.getResultList();
			List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList = new ArrayList<PtwRequiredDocumentDto>();
			for (Object[] o : list) {
				PtwRequiredDocumentDto ptwRequiredDocumentDto = new PtwRequiredDocumentDto();
				ptwRequiredDocumentDto.setSerialNo(Integer.parseInt(o[0].toString()));
				ptwRequiredDocumentDto.setPermitNumber(Integer.parseInt(o[1].toString()));
				ptwRequiredDocumentDto.setIsCWP(Integer.parseInt(o[2].toString()));
				ptwRequiredDocumentDto.setIsHWP(Integer.parseInt(o[3].toString()));
				ptwRequiredDocumentDto.setIsCSE(Integer.parseInt(o[4].toString()));
				ptwRequiredDocumentDto.setAtmosphericTestRecord(Integer.parseInt(o[5].toString()));
				ptwRequiredDocumentDto.setLoto(Integer.parseInt(o[6].toString()));
				ptwRequiredDocumentDto.setProcedure(Integer.parseInt(o[7].toString()));
				ptwRequiredDocumentDto.setPAndIdOrDrawing(Integer.parseInt(o[8].toString()));
				ptwRequiredDocumentDto.setCertificate((String) o[9]);
				ptwRequiredDocumentDto.setTemporaryDefeat(Integer.parseInt(o[10].toString()));
				ptwRequiredDocumentDto.setRescuePlan(Integer.parseInt(o[11].toString()));
				ptwRequiredDocumentDto.setSds(Integer.parseInt(o[12].toString()));
				ptwRequiredDocumentDto.setOtherWorkPermitDocs((String) o[13]);
				ptwRequiredDocumentDto.setFireWatchChecklist(Integer.parseInt(o[14].toString()));
				ptwRequiredDocumentDto.setLiftPlan(Integer.parseInt(o[15].toString()));
				ptwRequiredDocumentDto.setSimopDeviation(Integer.parseInt(o[16].toString()));
				ptwRequiredDocumentDto.setSafeWorkPractice(Integer.parseInt(o[17].toString()));
				ptwRequiredDocumentDtoList.add(ptwRequiredDocumentDto);
			}
			return ptwRequiredDocumentDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
