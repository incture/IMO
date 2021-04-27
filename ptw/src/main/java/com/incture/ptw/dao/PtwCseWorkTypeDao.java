package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwCseWorkTypeDto;

@Repository
public class PtwCseWorkTypeDao extends BaseDao {
	public void insertPtwCseWorkType(String permitNumber, PtwCseWorkTypeDto ptwCseWorkTypeDto) {
		try {
			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTW_CSE_WORK_TYPE\" VALUES (?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwCseWorkTypeDto.getTank());
			query.setParameter(3, ptwCseWorkTypeDto.getVessel());
			query.setParameter(4, ptwCseWorkTypeDto.getExcavation());
			query.setParameter(5, ptwCseWorkTypeDto.getPit());
			query.setParameter(6, ptwCseWorkTypeDto.getTower());
			query.setParameter(7, ptwCseWorkTypeDto.getOther());
			query.setParameter(8, ptwCseWorkTypeDto.getReasonForCSE());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public PtwCseWorkTypeDto getPtwCseWork(String permitNumber) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
