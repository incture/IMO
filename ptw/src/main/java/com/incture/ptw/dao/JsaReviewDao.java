package com.incture.ptw.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaReviewDto;

@Repository
public class JsaReviewDao extends BaseDao {
	public void insertJsaReview(String permitNumber, JsaReviewDto jsaReviewDto) {
		try {
			String sql = "INSERT INTO IOP.JSAREVIEW VALUES (?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("sql: " + sql);
			query.setParameter(1, Integer.parseInt(permitNumber));
			query.setParameter(2, jsaReviewDto.getCreatedBy());
			query.setParameter(3, jsaReviewDto.getApprovedBy());
			query.setParameter(4, jsaReviewDto.getApprovedDate());
			query.setParameter(5, jsaReviewDto.getLastUpdatedBy());
			query.setParameter(6, jsaReviewDto.getCreatedDate());
			query.setParameter(7, jsaReviewDto.getCreatedDate());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void updateJsaReview(String permitNumber, JsaReviewDto jsaReviewDto) {
		try {
			String sql = "UPDATE IOP.JSAREVIEW SET APPROVEDDATE = ? , LASTUPDATEDDATE = ? , APPROVEDBY= ? where PERMITNUMBER=?";
			logger.info("updateJsaReview sql :" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, new Date());
			query.setParameter(2, new Date());
			query.setParameter(3, jsaReviewDto.getApprovedBy());
			query.setParameter(4, permitNumber);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public JsaReviewDto getJsaReview(String permitNumber) {
		JsaReviewDto jsaReviewDto = new JsaReviewDto();
		List<Object[]> obj;
		try {
			String sql = "select * from IOP.JSAREVIEW where PERMITNUMBER:=permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber",permitNumber );
			logger.info("getJsaReview sql :" + sql);
			obj = query.getResultList();
			for (Object[] a : obj) {
				jsaReviewDto.setPermitNumber(Integer.parseInt( a[0].toString()));
				jsaReviewDto.setCreatedBy((String) a[1]);
				jsaReviewDto.setApprovedBy((String) a[2]);
				jsaReviewDto.setApprovedDate((Date) a[3]);
				jsaReviewDto.setLastUpdatedBy((String) a[4]);
				jsaReviewDto.setLastUpdatedDate( (Date) a[5]);
				jsaReviewDto.setCreatedDate((Date) a[6]);
			}
			logger.info("JsaReviewDto: "+jsaReviewDto);
			return jsaReviewDto;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
