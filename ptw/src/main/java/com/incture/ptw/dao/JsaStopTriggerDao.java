package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaStopTriggerDto;

@Repository
public class JsaStopTriggerDao extends BaseDao{
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaStopTrigger(String permitNumber,JsaStopTriggerDto JsaStopTriggerDto) {
		try {
			logger.info("JsaStopTriggerDto: " + JsaStopTriggerDto);
			String sql = "INSERT INTO \"IOP\".\"JSASTOPTRIGGER\" VALUES (?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, keyGeneratorDao.getJSASTOPSerialNo());
			query.setParameter(2, permitNumber);
			query.setParameter(3, JsaStopTriggerDto.getLineDescription());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

}
