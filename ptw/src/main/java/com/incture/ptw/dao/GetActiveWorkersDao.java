package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("GetActiveWorkersDao")
public class GetActiveWorkersDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public List<Object[]> getActiveWorkers(String muwi, String facility) {
		String sql = "";
		int flag = 0;
		if (muwi == null) {
			flag = 1;
			sql = " select P.FIRSTNAME P.LASTNAME P.CONTACTNUMBER P.PERMITNUMBER L.FACILTYORSITE from IOP.PTWPEOPLE as P inner join "
					+ " IOP.JSAHEADER as J on P.PERMITNUMBER = J.PERMITNUMBER inner join "
					+ " IOP.JSA_LOCATION as L on P.PERMITNUMBER = L.PERMITNUMBER "
					+ " WHERE L.FACILITY = :facility AND (J.ISACTIVE = in (1,2)) ";
		} else {
			flag = 2;
			sql = " select P.FIRSTNAME P.LASTNAME P.CONTACTNUMBER P.PERMITNUMBER L.FACILTYORSITE from IOP.PTWPEOPLE as P inner join "
					+ " IOP.JSAHEADER as J on P.PERMITNUMBER = J.PERMITNUMBER inner join "
					+ " IOP.JSA_LOCATION as L on P.PERMITNUMBER = L.PERMITNUMBER "
					+ " WHERE (L.MUWI = :muwi OR (L.FACILITY = :facility AND L.MUWI = 'null' )) AND (J.ISACTIVE in(1,2)) ";
		}
		logger.info("getActiveWorkers sql " + sql);

		try {
			Query q = getSession().createNativeQuery(sql);
			if (flag == 1) {
				q.setParameter("facility", facility);
			}
			if (flag == 2) {
				q.setParameter("muwi", muwi);
				q.setParameter("facility", facility);
			}
			List<Object[]> obj = q.getResultList();
			return obj;
		} catch (Exception e) {
			logger.error("getActiveWorkers error" + e.getMessage());
		}
		return null;
	}

}
