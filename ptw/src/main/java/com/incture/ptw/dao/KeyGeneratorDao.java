package com.incture.ptw.dao;

import java.math.BigInteger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class KeyGeneratorDao extends BaseDao {
	public BigInteger getPtwApprovalSerialNo() {
		try {
			String sql2 = "select IOP.PTWAPPROVAL_SEQ.NEXTVAL FROM DUMMY";
			Query q2 = getSession().createNativeQuery(sql2);
			return (BigInteger) q2.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getPermitNumber() {
		try {
			String sql = "select IOP.PERMITNUMBER_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	public String getSerialNo() {
		try {
			String sql = "select IOP.SERIALNO_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return  q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	public String getJSASTEPSSerialNo() {
		try {
			String sql = "select IOP.JSASTEPS_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getJSASTOPSerialNo() {
		try {
			String sql = "select IOP.JSASTOP_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getPTWREQDOC() {
		try {
			String sql = "select IOP.PTWREQDOC_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
//	public String getPTWAPPROVAL() {
//		try {
//			String sql = "select IOP.PTWAPPROVAL_SEQ.NEXTVAL FROM DUMMY";
//			Query q = getSession().createNativeQuery(sql);
//			return q.getSingleResult().toString();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return null;
//	}
	
	public String getPTWTESTREC() {
		try {
			String sql = "select IOP.PTWTESTREC_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getPTWATESTRES()  {
		try {
			String sql = "select IOP.PTWATESTRES_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getTOPTWCLOSEOUT()  {
		try {
			String sql = "select IOP.CLOSE_OUT_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	public String getTOJSALOCATION()  {
		try {
			String sql = "select IOP.JSA_LOCATION_SEQ.NEXTVAL FROM DUMMY";
			Query q = getSession().createNativeQuery(sql);
			return q.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
