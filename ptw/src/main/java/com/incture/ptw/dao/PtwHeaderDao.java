package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwHeaderDto;

@Repository
public class PtwHeaderDao extends BaseDao {
	public void insertPtwHeader(PtwHeaderDto ptwHeaderDto){
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"PTWHEADER\"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		query.setParameter(1, ptwHeaderDto.getPermitNumber());
		query.setParameter(2, ptwHeaderDto.getPtwPermitNumber());
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
	}

}
