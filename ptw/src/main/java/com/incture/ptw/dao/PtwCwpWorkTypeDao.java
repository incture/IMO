package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwCwpWorkTypeDto;

@Repository
public class PtwCwpWorkTypeDao extends BaseDao {
	public void insertPtwCwpWorkType(String permitNumber, PtwCwpWorkTypeDto ptwCwpWorkTypeDto) {
		try {
			Query query = getSession().createNativeQuery(
					"INSERT INTO \"IOP\".\"PTW_CWP_WORK_TYPE\"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwCwpWorkTypeDto.getCriticalOrComplexLift());
			query.setParameter(3, ptwCwpWorkTypeDto.getCraneOrLiftingDevice());
			query.setParameter(4, ptwCwpWorkTypeDto.getGroundDisturbanceOrExcavation());
			query.setParameter(5, ptwCwpWorkTypeDto.getHandlingHazardousChemicals());
			query.setParameter(6, ptwCwpWorkTypeDto.getWorkingAtHeight());
			query.setParameter(7, ptwCwpWorkTypeDto.getPaintingOrBlasting());
			query.setParameter(8, ptwCwpWorkTypeDto.getWorkingOnPressurizedSystems());
			query.setParameter(9, ptwCwpWorkTypeDto.getErectingOrDismantlingScaffodling());
			query.setParameter(10, ptwCwpWorkTypeDto.getBreakingContainmentOfClosedOperatingSystem());
			query.setParameter(11, ptwCwpWorkTypeDto.getWorkingInCloseToHazardousEnergy());
			query.setParameter(12, ptwCwpWorkTypeDto.getRemovalOFIdelEquipemntForRepair());
			query.setParameter(13, ptwCwpWorkTypeDto.getHigherRiskElectricalWork());
			query.setParameter(14, ptwCwpWorkTypeDto.getOtherTypeOfWork());
			query.setParameter(15, ptwCwpWorkTypeDto.getDescriptionOfWorkToBePerformed());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	public PtwCwpWorkTypeDto getPtwCwpWork(String permitNumber) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
