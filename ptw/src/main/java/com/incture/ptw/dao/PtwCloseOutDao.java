package com.incture.ptw.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwCloseOutDto;

@Repository
public class PtwCloseOutDao extends BaseDao{
	public List<PtwCloseOutDto> getPtwCloseOut(String permitNumber) {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
