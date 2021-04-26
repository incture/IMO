package com.incture.ptw.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwApprovalDto;


@Repository
public class PtwApprovalDao extends BaseDao{
	public List<PtwApprovalDto> getPtwApproval(String permitNumber, String isCwp, String isHwp, String isCse) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	

}
