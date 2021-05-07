package com.incture.ptw.dao;

import java.util.List;

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
			String sql = "select * from IOP.PTW_CSE_WORK_TYPE where PERMITNUMBER = ?";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			logger.info(permitNumber);
			logger.info("getPtwCseWork Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			logger.info(result.toString());
			if (result.isEmpty())
				return null;
			PtwCseWorkTypeDto ptwCseWorkTypeDto = new PtwCseWorkTypeDto();
			for (Object[] res : result) {
				ptwCseWorkTypeDto.setPermitNumber(Integer.parseInt(res[0].toString()));
				ptwCseWorkTypeDto.setTank(Integer.parseInt(res[1].toString()));
				ptwCseWorkTypeDto.setVessel(Integer.parseInt(res[2].toString()));
				ptwCseWorkTypeDto.setExcavation(Integer.parseInt(res[3].toString()));
				ptwCseWorkTypeDto.setPit(Integer.parseInt(res[4].toString()));
				ptwCseWorkTypeDto.setTower(Integer.parseInt(res[5].toString()));
				ptwCseWorkTypeDto.setOther((String) res[6]);
				ptwCseWorkTypeDto.setReasonForCSE((String) res[7]);
				break;
			}
			logger.info(ptwCseWorkTypeDto.toString());
			return ptwCseWorkTypeDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void updatePtwCseWorkType(PtwCseWorkTypeDto ptwCseWorkTypeDto) {
		try {
			String sql = "UPDATE \"IOP\".\"PTW_CSE_WORK_TYPE\" SET  \"TANK\"= ?, \"VESSEL\"= ?, \"EXCAVATION\"= ?, \"PIT\"= ?, \"TOWER\"= ?, \"OTHER\"= ?, \"REASONFORCSE\"= ? where \"PERMITNUMBER\"= ?";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, ptwCseWorkTypeDto.getTank());
			query.setParameter(2, ptwCseWorkTypeDto.getVessel());
			query.setParameter(3, ptwCseWorkTypeDto.getExcavation());
			query.setParameter(4, ptwCseWorkTypeDto.getPit());
			query.setParameter(5, ptwCseWorkTypeDto.getTower());
			query.setParameter(6, ptwCseWorkTypeDto.getOther());
			query.setParameter(7, ptwCseWorkTypeDto.getReasonForCSE());
			query.setParameter(8, ptwCseWorkTypeDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
