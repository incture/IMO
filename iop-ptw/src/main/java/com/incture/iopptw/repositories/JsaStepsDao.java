package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaStepsDto;

@Repository
public class JsaStepsDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaSteps(String permitNumber, JsaStepsDto jsaStepsDto) {
		try {
			logger.info("JsaStepsDto: " + jsaStepsDto);
			String sql = "INSERT INTO \"IOP\".\"JSASTEPS\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, keyGeneratorDao.getJSASTEPSSerialNo());
			query.setParameter(2, permitNumber);
			query.setParameter(3, jsaStepsDto.getTaskSteps());
			query.setParameter(4, jsaStepsDto.getPotentialHazards());
			query.setParameter(5, jsaStepsDto.getHazardControls());
			query.setParameter(6, jsaStepsDto.getPersonResponsible());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public List<JsaStepsDto> getJsaStepsDto(String permitNum) {
		List<Object[]> obj;
		List<JsaStepsDto> jsaStepsDtoList = new ArrayList<JsaStepsDto>();
		try {
			String sql = "select  SERIALNO,PERMITNUMBER, TASKSTEPS,POTENTIALHAZARDS,HAZARDCONTROLS,PERSONRESPONSIBLE "
					+ " from IOP.JSASTEPS where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				JsaStepsDto jsaStepsDto = new JsaStepsDto();
				jsaStepsDto.setSerialNo((Integer) a[0]);
				jsaStepsDto.setPermitNumber((Integer) a[1]);
				jsaStepsDto.setTaskSteps((String) a[2]);
				jsaStepsDto.setPotentialHazards((String) a[3]);
				jsaStepsDto.setHazardControls((String) a[4]);
				jsaStepsDto.setPersonResponsible((String) a[5]);
				jsaStepsDtoList.add(jsaStepsDto);
			}
			return jsaStepsDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deleteJsaSteps(String permitNumber) {
		try {
			logger.info("permitNumber: " + permitNumber);
			String sql = "DELETE FROM \"IOP\".\"JSASTEPS\" WHERE PERMITNUMBER =? ";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, permitNumber);
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

	}

}
