package com.incture.ptw.dao;

import java.util.List;

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
			query.setParameter(12, ptwCwpWorkTypeDto.getRemovalOfIdelEquipemntForRepair());
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
			String sql = "select * from IOP.PTW_CWP_WORK_TYPE where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwHwpWork Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			PtwCwpWorkTypeDto ptwCwpWorkTypeDto = new PtwCwpWorkTypeDto();
			for(Object[] a : result){
				ptwCwpWorkTypeDto.setPermitNumber((Integer)a[0]);
				ptwCwpWorkTypeDto.setCriticalOrComplexLift(Integer.parseInt(a[1].toString()));
				ptwCwpWorkTypeDto.setCraneOrLiftingDevice(Integer.parseInt(a[2].toString()));
				ptwCwpWorkTypeDto.setGroundDisturbanceOrExcavation(Integer.parseInt(a[3].toString()));
				ptwCwpWorkTypeDto.setHandlingHazardousChemicals(Integer.parseInt(a[4].toString()));
				ptwCwpWorkTypeDto.setWorkingAtHeight(Integer.parseInt(a[5].toString()));
				ptwCwpWorkTypeDto.setPaintingOrBlasting(Integer.parseInt(a[6].toString()));
				ptwCwpWorkTypeDto.setWorkingOnPressurizedSystems(Integer.parseInt(a[7].toString()));
				ptwCwpWorkTypeDto.setErectingOrDismantlingScaffodling(Integer.parseInt(a[8].toString()));
				ptwCwpWorkTypeDto.setBreakingContainmentOfClosedOperatingSystem(Integer.parseInt(a[9].toString()));
				ptwCwpWorkTypeDto.setWorkingInCloseToHazardousEnergy(Integer.parseInt(a[10].toString()));
				ptwCwpWorkTypeDto.setRemovalOfIdelEquipemntForRepair(Integer.parseInt(a[11].toString()));
				ptwCwpWorkTypeDto.setHigherRiskElectricalWork(Integer.parseInt(a[12].toString()));
				ptwCwpWorkTypeDto.setOtherTypeOfWork((String)a[13]);
				ptwCwpWorkTypeDto.setDescriptionOfWorkToBePerformed((String)a[14]);
			}
			return ptwCwpWorkTypeDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
