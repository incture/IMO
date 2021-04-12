package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("GetJsaByLocationDao")
public class JsaByLocationDao extends BaseDao {
	@SuppressWarnings("unchecked")
	public List<String> getPermitNumberList(String muwi, String facility) {
		String sql = "";
		if (muwi.isEmpty() || muwi.equals("null")) {
			sql = "select L.PERMITNUMBER from IOP.JSA_LOCATION as L inner join "
					+ " IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER inner join"
					+ " IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER " + " where (L.MUWI ='" + muwi
					+ "' or (L.FACILITY = '" + facility + "' and L.MUWI = 'null' ))"
					+ " AND (J.ISACTIVE = 1 or J.ISACTIVE = 2) ORDER BY R.LASTUPDATEDDATE DESC ";
		} else {
			sql = "select L.PERMITNUMBER from IOP.JSA_LOCATION as L inner join "
					+ " IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER inner join"
					+ " IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER " + " where (L.FACILITY = '" + facility
					+ "')" + "AND (J.ISACTIVE = 1 or J.ISACTIVE = 2) ORDER BY R.LASTUPDATEDDATE DESC ";
		}
		System.out.println("sql : " +sql);
		Query q = getSession().createNativeQuery(sql);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getJsaByLocation(String muwi, String facility) {
		List<String> permitNumberList = getPermitNumberList(muwi, facility);
		String sql = "select J.JSAPERMITNUMBER, J.TASKDESCRIPTION,J.STATUS,P.PTWPERMITNUMBER, "
				+ "R.CREATEDDATE,R.CREATEDBY , L.FACILTYORSITE, R.LASTUPDATEDDATE, "
				+ "R.APPROVEDDATE,J.PERMITNUMBER from IOP.JSA_LOCATION as L inner join "
				+ "IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER "
				+ "left join IOP.PTWHEADER as P on L.PERMITNUMBER = P.PERMITNUMBER "
				+ "inner join IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER "
				+ "where J.PERMITNUMBER IN (:list) ORDER BY R.LASTUPDATEDDATE DESC ";
		Query q = getSession().createNativeQuery(sql);
		q.setParameter("list", permitNumberList);
		return q.getResultList();
	}
}
