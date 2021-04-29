package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository("JsaDetailsDao")
public class JsaDetailsDao extends BaseDao {
	@SuppressWarnings("unchecked")
	public List<Object[]> downloadData() {
		try {
			Query query = getSession().createNativeQuery(
					"select J.*,L.FACILTYORSITE, R.CREATEDBY,R.APPROVEDBY,R.APPROVEDDATE,R.LASTUPDATEDBY,R.LASTUPDATEDDATE,R.CREATEDDATE"
							+ " from IOP.JSAHEADER as J inner join IOP.JSA_LOCATION as L on J.PERMITNUMBER=L.PERMITNUMBER"
							+ " inner join IOP.JSAREVIEW as R on J.PERMITNUMBER=R.PERMITNUMBER");
			List<Object[]> res = query.getResultList();

			System.out.println(res.toString());
			// for(Object[] row : res){
			// JsaheaderDto jsaheaderDto = new JsaheaderDto();
			// jsaheaderDto.setPermitNumber((Integer) row[0]);
			//
			//
			// }
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
		return null;
	}

}
