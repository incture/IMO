package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwHeaderDto;

@Repository
public class PtwHeaderDao extends BaseDao {
	public void insertPtwHeader(String permitNumber, String ptwHeader, PtwHeaderDto ptwHeaderDto) {
		try {

			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTWHEADER\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwHeader);
			// query.setParameter(2, ptwHeaderDto.getPtwPermitNumber());
			query.setParameter(3, ptwHeaderDto.getIsCwp());
			query.setParameter(4, ptwHeaderDto.getIsHwp());
			query.setParameter(5, ptwHeaderDto.getIsCse());
			query.setParameter(6, ptwHeaderDto.getPlannedDateTime());
			query.setParameter(7, ptwHeaderDto.getLocation());
			query.setParameter(8, ptwHeaderDto.getCreatedBy());
			query.setParameter(9, ptwHeaderDto.getContractorPerformingWork());
			query.setParameter(10, ptwHeaderDto.getEstimatedTimeOfCompletion());
			query.setParameter(11, ptwHeaderDto.getEquipmentId());
			query.setParameter(12, ptwHeaderDto.getWorkOrderNumber());
			query.setParameter(13, ptwHeaderDto.getStatus());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	public PtwHeaderDto getPermitNumber(String ptwPermitNumber) {
		PtwHeaderDto ptwHeaderDto = new PtwHeaderDto();
		try {
			String sql = "select PERMITNUMBER,ISCWP,ISHWP,ISCSE from IOP.PTWHEADER where PTWPERMITNUMBER:=ptwPermitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("ptwPermitNumber", ptwPermitNumber);
			logger.info("Sql: " + sql);
			List<Object[]> result = query.getResultList();
			for (Object res[] : result) {
				ptwHeaderDto.setPermitNumber(Integer.parseInt(res[0].toString()));
				ptwHeaderDto.setIsCwp(Integer.parseInt(res[1].toString()));
				ptwHeaderDto.setIsHwp(Integer.parseInt(res[1].toString()));
				ptwHeaderDto.setIsCse(Integer.parseInt(res[2].toString()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ptwHeaderDto;
	}

	public List<PtwHeaderDto> getPtwHeader(String ptwPermitNumber) {

		try {

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
