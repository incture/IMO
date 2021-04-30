package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwApprovalDto;

@Repository
public class PtwApprovalDao extends BaseDao {

	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public List<PtwApprovalDto> getPtwApproval(String permitNumber, String isCwp, String isHwp, String isCse) {

		try {
			String sql = "select * from IOP.PTWAPPROVAL where PERMITNUMBER= ? and ISCWP = ? and ISHWP = ? and ISCSE = ?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			query.setParameter(2, isCwp);
			query.setParameter(3, isHwp);
			query.setParameter(4, isCse);
			logger.info("getPtwApproval sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> list = query.getResultList();
			List<PtwApprovalDto> ptwRequiredDocumentDtoList = new ArrayList<PtwApprovalDto>();
			for (Object[] o : list) {
				PtwApprovalDto ptwApprovalDto = new PtwApprovalDto();
				ptwApprovalDto.setSerialNo(Integer.parseInt(o[0].toString()));
				ptwApprovalDto.setPermitNumber(Integer.parseInt(o[1].toString()));
				ptwApprovalDto.setIsCWP(Integer.parseInt(o[2].toString()));
				ptwApprovalDto.setIsHWP(Integer.parseInt(o[3].toString()));
				ptwApprovalDto.setIsCSE(Integer.parseInt(o[4].toString()));
				ptwApprovalDto.setIsWorkSafeToPerform((int) o[5]);
				ptwApprovalDto.setPreJobWalkThroughBy((String) o[6]);
				ptwApprovalDto.setApprovedBy((String) o[7]);
				ptwApprovalDto.setApprovalDate((Date) o[8]);
				ptwApprovalDto.setControlBoardDistribution((int) o[9]);
				ptwApprovalDto.setWorkSiteDistribution((int) o[10]);
				ptwApprovalDto.setSimopsDistribution((int) o[11]);
				ptwApprovalDto.setOtherDistribution((String) o[12]);
				ptwApprovalDto.setPicName((String) o[13]);
				ptwApprovalDto.setPicDate((Date) o[14]);
				ptwApprovalDto.setSuperitendentDate((Date) o[15]);
				ptwApprovalDto.setSuperitendentName((String) o[16]);
				ptwRequiredDocumentDtoList.add(ptwApprovalDto);

			}
			return ptwRequiredDocumentDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void insertPtwApproval(String permitNumber, PtwApprovalDto i) {
		try {
			String sql = "INSERT INTO \"IOP\".\"PTWAPPROVAL\" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, Integer.parseInt(keyGeneratorDao.getPTWAPPROVAL()));
			query.setParameter(2, Integer.parseInt(permitNumber));
			query.setParameter(3, i.getIsCWP());
			query.setParameter(4, i.getIsHWP());
			query.setParameter(5, i.getIsCSE());
			query.setParameter(6, i.getIsWorkSafeToPerform());
			query.setParameter(7, i.getPreJobWalkThroughBy());
			query.setParameter(8, i.getApprovedBy());
			query.setParameter(9, i.getApprovalDate());
			query.setParameter(10, i.getControlBoardDistribution());
			query.setParameter(11, i.getWorkSiteDistribution());
			query.setParameter(12, i.getSimopsDistribution());
			query.setParameter(13, i.getOtherDistribution());
			query.setParameter(14, i.getPicName());
			query.setParameter(15, i.getPicDate());
			query.setParameter(16, i.getSuperitendentName());
			query.setParameter(17, i.getSuperitendentDate());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}
