package com.incture.ptw.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaheaderDto;

@Repository
public class JsaHeaderDao extends BaseDao {
	private void insertJsaHeader(JsaheaderDto jsaheaderDto){
		Query query = getSession().createNativeQuery("INSERT INTO \"IOP\".\"JSAREVIEW\" VALUES (?,?,?,?,?,?,?)");
		//query.setParameter(arg0, arg1)
	} 

}
