package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwHwpWorkTypeDto;

@Repository
public class PtwHwpWorkTypeDao extends BaseDao {
	public void insertPtwHwpWorkType(String permitNumber, PtwHwpWorkTypeDto ptwHwpWorkTypeDto) {
		try {
			Query query = getSession()
					.createNativeQuery("INSERT INTO \"IOP\".\"PTW_HWP_WORK_TYPE\" VALUES (?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwHwpWorkTypeDto.getCutting());
			query.setParameter(3, ptwHwpWorkTypeDto.getWielding());
			query.setParameter(4, ptwHwpWorkTypeDto.getElectricalPoweredEquipment());
			query.setParameter(5, ptwHwpWorkTypeDto.getGrinding());
			query.setParameter(6, ptwHwpWorkTypeDto.getAbrasiveBlasting());
			query.setParameter(7, ptwHwpWorkTypeDto.getOtherTypeOfWork());
			query.setParameter(8, ptwHwpWorkTypeDto.getDescriptionOfWorkToBePerformed());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
}
