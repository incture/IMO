package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("PermitsByLocDao")
public class PermitsByLocDao extends BaseDao {
	@Autowired
	private JsaByLocationDao jsaByLocationDao;

	@SuppressWarnings("unchecked")
	public List<Object[]> getPermitsByLoc(String muwi, String facility) {

		List<String> permitNumberList = jsaByLocationDao.getPermitNumberList(muwi, facility);
		logger.info("getpermitnumberlist :" + permitNumberList);
		if (permitNumberList.isEmpty()) {
			return null;
		}
		String sql = " select J.JSAPERMITNUMBER,P.PTWPERMITNUMBER,P.CREATEDBY,P.ISCWP, "
				+ "P.ISHWP,P.ISCSE,R.CREATEDDATE,L.FACILTYORSITE,J.PERMITNUMBER,R.LASTUPDATEDDATE, "
				+ "P.STATUS FROM IOP.JSA_LOCATION as L inner join IOP.JSAHEADER as J "
				+ "on L.PERMITNUMBER = J.PERMITNUMBER left join IOP.PTWHEADER as P "
				+ "on L.PERMITNUMBER = P.PERMITNUMBER inner join IOP.JSAREVIEW as R "
				+ "on L.PERMITNUMBER = R.PERMITNUMBER where J.PERMITNUMBER IN (:list) ORDER BY R.CREATEDDATE DESC ";
		Query q = getSession().createNativeQuery(sql);
		logger.info("PermitsByLocDao | getPermitsByLoc | sql  " + sql);
		q.setParameter("list", permitNumberList);
		@SuppressWarnings("unchecked")
		List<Object[]> obj = q.getResultList();
		return obj;
	}

}
