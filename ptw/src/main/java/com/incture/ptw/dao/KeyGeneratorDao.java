package com.incture.ptw.dao;

import java.math.BigInteger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class KeyGeneratorDao extends BaseDao {
	public BigInteger getSerialNo() {
		try {
			String sql2 = "select IOP.PTWAPPROVAL_SEQ.NEXTVAL FROM DUMMY";
			Query q2 = getSession().createNativeQuery(sql2);
			return (BigInteger) q2.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
