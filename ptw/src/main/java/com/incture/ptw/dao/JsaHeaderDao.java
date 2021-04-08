package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaheaderDto;

@Repository
public class JsaHeaderDao extends BaseDao {
	private void insertJsaHeader(JsaheaderDto jsaheaderDto){
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"JSAHEADER\" VALUES (?,?,?,?,?,?,?,?,?)");
		query.setParameter(1, jsaheaderDto.getPermitNumber());
		query.setParameter(2, jsaheaderDto.getJsaPermitNumber());
		query.setParameter(3, jsaheaderDto.getHasCwp());
		query.setParameter(4, jsaheaderDto.getHasHwp());
		query.setParameter(5, jsaheaderDto.getHasCse());
		query.setParameter(6, jsaheaderDto.getTaskDescription());
		query.setParameter(7, jsaheaderDto.getIdentifyMostSeriousPotentialInjury());
		query.setParameter(8, jsaheaderDto.getIsActive());
		query.setParameter(9, jsaheaderDto.getStatus());
		query.executeUpdate();		
	} 

}
