package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.PtwCwpWorkTypeDto;

@Repository
public class PtwCwpWorkTypeDao extends BaseDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void insertPtwCwpWorkType(String permitNumber, PtwCwpWorkTypeDto ptwCwpWorkTypeDto) {
		logger.info("ptwCwpWorkTypeDto" + ptwCwpWorkTypeDto);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(
					"INSERT INTO \"IOP\".\"PTW_CWP_WORK_TYPE\"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			query.setParameter(1, permitNumber);
			query.setParameter(2, ptwCwpWorkTypeDto.getCriticalOrComplexLift());
			query.setParameter(3, ptwCwpWorkTypeDto.getCraneOrLiftingDevice());
			query.setParameter(4, ptwCwpWorkTypeDto.getGroundDisturbanceOrExcavation());
			query.setParameter(5, ptwCwpWorkTypeDto.getHandlingHazardousChemicals());
			query.setParameter(6, ptwCwpWorkTypeDto.getWorkingAtHeight());
			query.setParameter(7, ptwCwpWorkTypeDto.getPaintingOrBlasting());
			query.setParameter(8, ptwCwpWorkTypeDto.getWorkingOnPressurizedSystems());
			query.setParameter(9, ptwCwpWorkTypeDto.getErectingOrDismantlingScaffolding());
			query.setParameter(10, ptwCwpWorkTypeDto.getBreakingContainmentOfClosedOperatingSystem());
			query.setParameter(11, ptwCwpWorkTypeDto.getWorkingInCloseToHazardousEnergy());
			query.setParameter(12, ptwCwpWorkTypeDto.getRemovalOfIdleEquipmentForRepair());
			query.setParameter(13, ptwCwpWorkTypeDto.getHigherRiskElectricalWork());
			query.setParameter(14, ptwCwpWorkTypeDto.getOtherTypeOfWork());
			query.setParameter(15, ptwCwpWorkTypeDto.getDescriptionOfWorkToBePerformed());
			query.executeUpdate();
			tx.commit();
			session.close();
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
			if (result.isEmpty())
				return null;
			PtwCwpWorkTypeDto ptwCwpWorkTypeDto = new PtwCwpWorkTypeDto();
			for (Object[] a : result) {
				ptwCwpWorkTypeDto.setPermitNumber(Integer.parseInt(a[0].toString()));
				ptwCwpWorkTypeDto.setCriticalOrComplexLift(Integer.parseInt(a[1].toString()));
				ptwCwpWorkTypeDto.setCraneOrLiftingDevice(Integer.parseInt(a[2].toString()));
				ptwCwpWorkTypeDto.setGroundDisturbanceOrExcavation(Integer.parseInt(a[3].toString()));
				ptwCwpWorkTypeDto.setHandlingHazardousChemicals(Integer.parseInt(a[4].toString()));
				ptwCwpWorkTypeDto.setWorkingAtHeight(Integer.parseInt(a[5].toString()));
				ptwCwpWorkTypeDto.setPaintingOrBlasting(Integer.parseInt(a[6].toString()));
				ptwCwpWorkTypeDto.setWorkingOnPressurizedSystems(Integer.parseInt(a[7].toString()));
				ptwCwpWorkTypeDto.setErectingOrDismantlingScaffolding(Integer.parseInt(a[8].toString()));
				ptwCwpWorkTypeDto.setBreakingContainmentOfClosedOperatingSystem(Integer.parseInt(a[9].toString()));
				ptwCwpWorkTypeDto.setWorkingInCloseToHazardousEnergy(Integer.parseInt(a[10].toString()));
				ptwCwpWorkTypeDto.setRemovalOfIdleEquipmentForRepair(Integer.parseInt(a[11].toString()));
				ptwCwpWorkTypeDto.setHigherRiskElectricalWork(Integer.parseInt(a[12].toString()));
				ptwCwpWorkTypeDto.setOtherTypeOfWork((String) a[13]);
				ptwCwpWorkTypeDto.setDescriptionOfWorkToBePerformed((String) a[14]);
				break;
			}
			logger.info("ptwCwpWorkTypeDto" + ptwCwpWorkTypeDto);
			return ptwCwpWorkTypeDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updatePtwCwpWorkType(PtwCwpWorkTypeDto ptwCwpWorkTypeDto) {
		try {
			String sql = "UPDATE \"IOP\".\"PTW_CWP_WORK_TYPE\" SET  \"CRITICALORCOMPLEXLIFT\"= ?, \"CRANEORLIFTINGDEVICE\"= ?, \"GROUNDDISTURBANCEOREXCAVATION\"= ?, \"HANDLINGHAZARDOUSCHEMICALS\"= ?, \"WORKINGATHEIGHT\"= ?, \"PAINTINGORBLASTING\"= ?, \"WORKINGONPRESSURIZEDSYSTEMS\"= ?, \"ERECTINGORDISMANTLINGSCAFFOLDING\"= ?, \"BREAKINGCONTAINMENTOFCLOSEDOPERATINGSYSTEM\"= ?, \"WORKINGINCLOSETOHAZARDOUSENERGY\"= ?, \"REMOVALOFIDLEEQUIPMENTFORREPAIR\"= ?, \"HIGHERRISKELECTRICALWORK\"= ?, \"OTHERTYPEOFWORK\"= ?, \"DESCRIPTIONOFWORKTOBEPERFORMED\"= ? where \"PERMITNUMBER\"= ?";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, ptwCwpWorkTypeDto.getCriticalOrComplexLift());
			query.setParameter(2, ptwCwpWorkTypeDto.getCraneOrLiftingDevice());
			query.setParameter(3, ptwCwpWorkTypeDto.getGroundDisturbanceOrExcavation());
			query.setParameter(4, ptwCwpWorkTypeDto.getHandlingHazardousChemicals());
			query.setParameter(5, ptwCwpWorkTypeDto.getWorkingAtHeight());
			query.setParameter(6, ptwCwpWorkTypeDto.getPaintingOrBlasting());
			query.setParameter(7, ptwCwpWorkTypeDto.getWorkingOnPressurizedSystems());
			query.setParameter(8, ptwCwpWorkTypeDto.getErectingOrDismantlingScaffolding());
			query.setParameter(9, ptwCwpWorkTypeDto.getBreakingContainmentOfClosedOperatingSystem());
			query.setParameter(10, ptwCwpWorkTypeDto.getWorkingInCloseToHazardousEnergy());
			query.setParameter(11, ptwCwpWorkTypeDto.getRemovalOfIdleEquipmentForRepair());
			query.setParameter(12, ptwCwpWorkTypeDto.getHigherRiskElectricalWork());
			query.setParameter(13, ptwCwpWorkTypeDto.getOtherTypeOfWork());
			query.setParameter(14, ptwCwpWorkTypeDto.getDescriptionOfWorkToBePerformed());
			query.setParameter(15, ptwCwpWorkTypeDto.getPermitNumber());
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
