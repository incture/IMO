package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaStepsDto;


@Repository
public class JsaStepsDao extends BaseDao{
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaSteps(String permitNumber,JsaStepsDto jsaStepsDto) {
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


}
