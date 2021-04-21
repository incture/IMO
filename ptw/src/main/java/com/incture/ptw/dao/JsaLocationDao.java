package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaLocationDto;

@Repository
public class JsaLocationDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaLocation(JsaLocationDto jsaLocationDto) {
		try {
			logger.info("JsaLocationDto: " + jsaLocationDto);
			String sql = "INSERT INTO \"IOP\".\"JSA_LOCATION\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, keyGeneratorDao.getTOJSALOCATION());
			query.setParameter(2, keyGeneratorDao.getPermitNumber());
			query.setParameter(3, jsaLocationDto.getFaciltyOrSite());
			query.setParameter(4, jsaLocationDto.getHierarchyLevel());
			query.setParameter(5, jsaLocationDto.getFacility());
			query.setParameter(6, jsaLocationDto.getMuwi());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}
}
