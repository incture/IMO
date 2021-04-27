package com.incture.ptw.dao;

import java.util.List;

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
	
	public PtwHwpWorkTypeDto getPtwHwpWork(String permitNumber) {
		try {
			String sql = "select * from IOP.PTW_HWP_WORK_TYPE where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwHwpWork Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			PtwHwpWorkTypeDto ptwHwpWorkTypeDto = new PtwHwpWorkTypeDto();
			for(Object[] a : result){
				ptwHwpWorkTypeDto.setPermitNumber(Integer.parseInt(a[0].toString()));
				ptwHwpWorkTypeDto.setCutting(Integer.parseInt(a[1].toString()));
				ptwHwpWorkTypeDto.setWielding(Integer.parseInt(a[2].toString()));
				ptwHwpWorkTypeDto.setElectricalPoweredEquipment(Integer.parseInt(a[3].toString()));
				ptwHwpWorkTypeDto.setGrinding(Integer.parseInt(a[4].toString()));
				ptwHwpWorkTypeDto.setAbrasiveBlasting(Integer.parseInt(a[5].toString()));
				ptwHwpWorkTypeDto.setOtherTypeOfWork((String)a[6]);
				ptwHwpWorkTypeDto.setDescriptionOfWorkToBePerformed((String)a[7]);
			}
			return ptwHwpWorkTypeDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
