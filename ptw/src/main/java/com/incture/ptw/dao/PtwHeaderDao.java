package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwApprovalDto;
import com.incture.ptw.dto.PtwCloseOutDto;
import com.incture.ptw.dto.PtwHeaderDto;

@Repository
public class PtwHeaderDao extends BaseDao {
	public void insertPtwHeader(String permitNumber, String ptwHeader, PtwHeaderDto ptwHeaderDto) {
		logger.info("ptwHeaderDto"+ptwHeaderDto);
		try {

			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTWHEADER\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwHeader);
			// query.setParameter(2, ptwHeaderDto.getPtwPermitNumber());
			query.setParameter(3, ptwHeaderDto.getIsCWP());
			query.setParameter(4, ptwHeaderDto.getIsHWP());
			query.setParameter(5, ptwHeaderDto.getIsCSE());
			query.setParameter(6, ptwHeaderDto.getPlannedDateTime());
			query.setParameter(7, ptwHeaderDto.getLocation());
			query.setParameter(8, ptwHeaderDto.getCreatedBy());
			query.setParameter(9, ptwHeaderDto.getContractorPerformingWork());
			query.setParameter(10, ptwHeaderDto.getEstimatedTimeOfCompletion());
			query.setParameter(11, ptwHeaderDto.getEquipmentID());
			query.setParameter(12, ptwHeaderDto.getWorkOrderNumber());
			query.setParameter(13, ptwHeaderDto.getStatus());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public PtwHeaderDto getPermitNumber(String ptwPermitNumber) {
		PtwHeaderDto ptwHeaderDto = new PtwHeaderDto();
		try {
			String sql = "select PERMITNUMBER,ISCWP,ISHWP,ISCSE from IOP.PTWHEADER where PTWPERMITNUMBER = ?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, ptwPermitNumber);
			logger.info("Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			for (Object[] res : result) {
				ptwHeaderDto.setPermitNumber(Integer.parseInt(res[0].toString()));
				ptwHeaderDto.setIsCWP(Integer.parseInt(res[1].toString()));
				ptwHeaderDto.setIsHWP(Integer.parseInt(res[2].toString()));
				ptwHeaderDto.setIsCSE(Integer.parseInt(res[3].toString()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ptwHeaderDto;
	}

	public List<PtwHeaderDto> getPtwHeader(String ptwPermitNumber) {
		List<PtwHeaderDto> ptwHeaderDtoList = new ArrayList<PtwHeaderDto>();
		try {
			String sql = "select * from IOP.PTWHEADER where PTWPERMITNUMBER  = ?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, ptwPermitNumber);
			logger.info("GetPtwHeader Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			for (Object[] res : result) {
				PtwHeaderDto ptwHeaderDto = new PtwHeaderDto();
				ptwHeaderDto.setPermitNumber(Integer.parseInt(res[0].toString()));
				ptwHeaderDto.setPtwPermitNumber((String) res[1]);
				ptwHeaderDto.setIsCWP(Integer.parseInt(res[2].toString()));
				ptwHeaderDto.setIsHWP(Integer.parseInt(res[3].toString()));
				ptwHeaderDto.setIsCSE(Integer.parseInt(res[4].toString()));
				ptwHeaderDto.setPlannedDateTime((Date) res[5]);
				ptwHeaderDto.setLocation((String) res[6]);
				ptwHeaderDto.setCreatedBy((String) res[7]);
				ptwHeaderDto.setContractorPerformingWork((String) res[8]);
				ptwHeaderDto.setEstimatedTimeOfCompletion((Date) res[9]);
				ptwHeaderDto.setEquipmentID((String) res[10]);
				ptwHeaderDto.setWorkOrderNumber((String) res[11]);
				ptwHeaderDto.setStatus((String) res[12]);
				ptwHeaderDtoList.add(ptwHeaderDto);
			}
			return ptwHeaderDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public void updatePtwHeader(PtwCloseOutDto ptwCloseOutDto, String status) {
		Integer permitNumber = ptwCloseOutDto.getPermitNumber();
		Integer isCwp = ptwCloseOutDto.getIsCWP();
		Integer isHwp = ptwCloseOutDto.getIsHWP();
		Integer isCse = ptwCloseOutDto.getIsCSE();
		try {

			Query query = getSession()
					.createNativeQuery("Update \"IOP\".\"PTWHEADER\" set \"STATUS\" = ?  where \"PERMITNUMBER\" = "+permitNumber +" and \"ISCWP\" = "+isCwp +" and \"ISHWP\" = "+isHwp +" and \"ISCSE\"="+isCse +"");
			query.setParameter(1, status);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void updatePtwHeader(PtwApprovalDto ptwApprovalDto, PtwHeaderDto ptwHeaderDto) {
		try {

			Query query = getSession()
					.createNativeQuery("Update \"IOP\".\"PTWHEADER\" set \"STATUS\" = ?  where \"PERMITNUMBER\" = ? and \"ISCWP\" = ? and \"ISHWP\" = ? and \"ISCSE\"= ? ");
			query.setParameter(1, ptwHeaderDto.getStatus());
			query.setParameter(2, ptwApprovalDto.getPermitNumber());
			query.setParameter(3, ptwApprovalDto.getIsCWP());
			query.setParameter(4, ptwApprovalDto.getIsHWP());
			query.setParameter(5, ptwApprovalDto.getIsCSE());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}

}
