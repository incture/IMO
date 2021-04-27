package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PtwCloseOutDto;

@Repository
public class PtwCloseOutDao extends BaseDao{
	public List<PtwCloseOutDto> getPtwCloseOut(String permitNumber) {
		try {
			String sql = "select * from IOP.PTWCLOSEOUT where PERMITNUMBER= :permitNumber";
			Query query = getSession().createNativeQuery(sql);
			query.setParameter("permitNumber", permitNumber);
			logger.info("getPtwHwpWork Sql: " + sql);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			List<PtwCloseOutDto> ptwCloseOutDtoList = new ArrayList<PtwCloseOutDto>();
			for(Object[] a : result){
				PtwCloseOutDto ptwCloseOutDto = new PtwCloseOutDto();
				ptwCloseOutDto.setSerialNo(Integer.parseInt(a[0].toString()));
				ptwCloseOutDto.setPermitNumber(Integer.parseInt(a[1].toString()));
				ptwCloseOutDto.setIsCWP(Integer.parseInt(a[2].toString()));
				ptwCloseOutDto.setIsHWP(Integer.parseInt(a[3].toString()));
				ptwCloseOutDto.setIsCSE(Integer.parseInt(a[4].toString()));
				ptwCloseOutDto.setPicName((String)a[5]);
				ptwCloseOutDto.setWorkCompleted(Integer.parseInt(a[6].toString()));
				ptwCloseOutDto.setClosedBy((String)a[7]);
				ptwCloseOutDto.setClosedDate((Date)a[8]);
				ptwCloseOutDto.setWorkStatusComment((String)a[9]);
				ptwCloseOutDtoList.add(ptwCloseOutDto);
			}
			return ptwCloseOutDtoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
