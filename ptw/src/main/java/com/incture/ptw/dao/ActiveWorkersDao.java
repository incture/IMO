package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.ActiveWorkersDto;
import com.incture.ptw.dto.ActiveWorkersPayloadDto;
import com.incture.ptw.dto.ActiveWorkersRecordDto;

@Repository("GetActiveWorkersDao")
public class ActiveWorkersDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public List<ActiveWorkersPayloadDto> getActiveWorkers(String muwi, String facility) {
		String sql = "";
		int flag = 0;
		if (muwi == null) {
			flag = 1;
			sql = " select P.FIRSTNAME,P.LASTTNAME,P.CONTACTNUMBER,P.PERMITNUMBER,L.FACILTYORSITE from IOP.PTWPEOPLE as P inner join "
					+ " IOP.JSAHEADER as J on P.PERMITNUMBER = J.PERMITNUMBER inner join "
					+ " IOP.JSA_LOCATION as L on P.PERMITNUMBER = L.PERMITNUMBER "
					+ " WHERE L.FACILITY = :facility AND (J.ISACTIVE in (1,2)) ";
		} else {
			flag = 2;
			sql = " select P.FIRSTNAME, P.LASTTNAME,P.CONTACTNUMBER,P.PERMITNUMBER,L.FACILTYORSITE from IOP.PTWPEOPLE as P inner join "
					+ " IOP.JSAHEADER as J on P.PERMITNUMBER = J.PERMITNUMBER inner join "
					+ " IOP.JSA_LOCATION as L on P.PERMITNUMBER = L.PERMITNUMBER "
					+ " WHERE (L.MUWI = :muwi OR (L.FACILITY = :facility AND L.MUWI = 'null' )) AND (J.ISACTIVE in(1,2)) ";
		}
		logger.info("getActiveWorkers sql " + sql);
		List<Object[]> obj;

		try {
			Query q = getSession().createNativeQuery(sql);
			if (flag == 1) {
				q.setParameter("facility", facility);
			}
			if (flag == 2) {
				q.setParameter("muwi", muwi);
				q.setParameter("facility", facility);
			}
			obj = q.getResultList();
			logger.info("getActiveWorkers sql output :" + obj);
			List<ActiveWorkersRecordDto> res = new ArrayList<ActiveWorkersRecordDto>();
			for (Object[] d : obj) {
				ActiveWorkersRecordDto temp = new ActiveWorkersRecordDto();
				temp.setFirstName((String) d[0]);
				temp.setLastName((String) d[1]);
				temp.setContactNumber((String) d[2]);
				temp.setPermitNumber((int) d[3]);
				temp.setFacilityOrSite((String) d[4]);
				res.add(temp);
			}
			Collections.sort(res, (a, b) -> {
				return a.getFacilityOrSite().compareTo(b.getFacilityOrSite());
			});
			logger.info("res" + res);
			List<ActiveWorkersPayloadDto> finalDataList = new ArrayList<ActiveWorkersPayloadDto>();
			ActiveWorkersPayloadDto facilitySiteData = new ActiveWorkersPayloadDto();
			for (int i = 0; i < res.size(); i++) {
				ActiveWorkersDto ptwPeopleData = new ActiveWorkersDto();
				List<ActiveWorkersDto> ptwPeopleList = new ArrayList<ActiveWorkersDto>();
				ptwPeopleData.setFirstName(res.get(i).getFirstName());
				ptwPeopleData.setLastName(res.get(i).getLastName());
				ptwPeopleData.setContactNumber(res.get(i).getContactNumber());
				ptwPeopleData.setPermitNumber(res.get(i).getPermitNumber());
				facilitySiteData.setFacilityOrSite(res.get(i).getFacilityOrSite());
				if (facilitySiteData.getPtwPeopleList()==null) {
					facilitySiteData.setPtwPeopleList(null);
				}
				if (i == (res.size() - 1)) {
					ptwPeopleList.add(ptwPeopleData);
					facilitySiteData.setPtwPeopleList(ptwPeopleList);
					finalDataList.add(facilitySiteData);
					break;
				} else if (res.get(i).getFacilityOrSite() == res.get(i + 1).getFacilityOrSite()) {
					ptwPeopleList.add(ptwPeopleData);
					facilitySiteData.setPtwPeopleList(ptwPeopleList);
				} else {
					ptwPeopleList.add(ptwPeopleData);
					facilitySiteData.setPtwPeopleList(ptwPeopleList);
					finalDataList.add(facilitySiteData);
					facilitySiteData = new ActiveWorkersPayloadDto();
					ptwPeopleList = new ArrayList<ActiveWorkersDto>();
				}

			}
			logger.info("finalDataList" + finalDataList);
			return finalDataList;
		} catch (Exception e) {
			logger.error("getActiveWorkers error" + e.getMessage());
		}
		return null;
	}

}
