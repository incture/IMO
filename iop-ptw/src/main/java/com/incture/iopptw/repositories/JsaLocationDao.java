package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaLocationDto;

@Repository
public class JsaLocationDao extends BaseDao {
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public void insertJsaLocation(String permitNumber, JsaLocationDto jsaLocationDto) {
		try {
			logger.info("JsaLocationDto: " + jsaLocationDto);
			String sql = "INSERT INTO \"IOP\".\"JSA_LOCATION\" VALUES (?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, Integer.parseInt(keyGeneratorDao.getTOJSALOCATION()));
			query.setParameter(2, Integer.parseInt(permitNumber));
			query.setParameter(3, jsaLocationDto.getFacilityOrSite());
			query.setParameter(4, jsaLocationDto.getHierachyLevel());
			query.setParameter(5, jsaLocationDto.getFacility());
			query.setParameter(6, jsaLocationDto.getMuwi());
			logger.info("sql " + sql);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	public List<JsaLocationDto> getJsaLocationDto(String permitNum) {
		List<Object[]> obj;
		List<JsaLocationDto> jsaLocationDtoList = new ArrayList<JsaLocationDto>();
		try {
			String sql = "select  SERIALNO,PERMITNUMBER, FACILTYORSITE,HIERARCHYLEVEL,FACILITY,MUWI from IOP.JSA_LOCATION "
					+ " where PERMITNUMBER = :permitNum";
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();

			for (Object[] a : obj) {
				JsaLocationDto jsaLocationDto = new JsaLocationDto();
				jsaLocationDto.setSerialNo((Integer) a[0]);
				jsaLocationDto.setPermitNumber((Integer) a[1]);
				jsaLocationDto.setFacilityOrSite((String) a[2]);
				jsaLocationDto.setHierachyLevel((String) a[3]);
				jsaLocationDto.setFacility((String) a[4]);
				jsaLocationDto.setMuwi((String) a[5]);
				jsaLocationDtoList.add(jsaLocationDto);
			}
			return jsaLocationDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void deleteJsaLocation(String permitNumber) {
		try {
			logger.info("permitNumber: " + permitNumber);
			String sql = "DELETE FROM \"IOP\".\"JSA_LOCATION\" WHERE PERMITNUMBER =? ";
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
