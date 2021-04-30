package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.incture.ptw.dto.JsaStopTriggerDto;

@Repository
public class JsaStopTriggerDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaStopTrigger(String permitNumber, JsaStopTriggerDto JsaStopTriggerDto) {
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

	@SuppressWarnings("unchecked")
	public List<JsaStopTriggerDto> getJsaStopTriggerDto(String permitNum) {
		List<Object[]> obj;
		List<JsaStopTriggerDto> jsaStopTriggerDtoList = new ArrayList<JsaStopTriggerDto>();
		try {
			String sql = "select  SERIALNO,PERMITNUMBER, LINEDESCRIPTION from IOP.JSASTOPTRIGGER "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			for (Object[] a : obj) {
				JsaStopTriggerDto jsaStopTriggerDto = new JsaStopTriggerDto();
				jsaStopTriggerDto.setSerialNo((Integer) a[0]);
				jsaStopTriggerDto.setPermitNumber((Integer) a[1]);
				jsaStopTriggerDto.setLineDescription((String) a[2]);
				jsaStopTriggerDtoList.add(jsaStopTriggerDto);
			}
			return jsaStopTriggerDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deleteJsaStopTrigger(String permitNumber) {
		try {
			logger.info("permitNumber: " + permitNumber);
			String sql = "DELETE FROM \"IOP\".\"JSASTOPTRIGGER\" WHERE PERMITNUMBER =? ";
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
