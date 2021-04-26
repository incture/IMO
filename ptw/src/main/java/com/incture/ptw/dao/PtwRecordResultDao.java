package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwRecordResultResponse;

@Repository
public class PtwRecordResultDao extends BaseDao {

	public PtwRecordResultResponse getPtwRecordResult(String permitNumber) {
		try {
			String sql1 = "select * from IOP.PTWTESTRESULTS where PERMITNUMBER= ?";
			Query q = getSession().createNativeQuery(sql1);
			logger.info("1st sql1 : " + sql1);
			q.setParameter(1, permitNumber);
			Object rs = q.getResultList();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
