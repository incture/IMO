package com.incture.ptw.dao;

import javax.persistence.Query;
import com.incture.ptw.dto.JsaReviewDto;

public class JsaReviewDao extends BaseDao {
	public void insertJsaReview(JsaReviewDto jsaReviewDto) {
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"JSAREVIEW\" VALUES (?,?,?,?,?,?,?)");
		query.setParameter(1, jsaReviewDto.getPermitNumber());
		query.setParameter(2, jsaReviewDto.getCreatedBy());
		query.setParameter(3, jsaReviewDto.getApprovedBy());
		query.setParameter(4, jsaReviewDto.getApprovedDate());
		query.setParameter(5, jsaReviewDto.getLastUpdatedBy());
		query.setParameter(6, jsaReviewDto.getLastUpdatedDate());
		query.setParameter(7, jsaReviewDto.getCreatedDate());

		query.executeUpdate();
	}

}
