package com.incture.ptw.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.JsaDetailsDto;
import com.incture.ptw.dto.JsaRecord;

@Repository("GetJsaByLocationDao")
public class JsaByLocationDao extends BaseDao {
	@SuppressWarnings("unchecked")
	public List<String> getPermitNumberList(String muwi, String facility) {
		String sql = "";
		if (muwi != null) {
			sql = "select L.PERMITNUMBER from IOP.JSA_LOCATION as L inner join "
					+ " IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER inner join"
					+ " IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER " + " where (L.MUWI ='" + muwi
					+ "' or (L.FACILITY = '" + facility + "' and L.MUWI = 'null' ))"
					+ " AND (J.ISACTIVE = 1 or J.ISACTIVE = 2) ORDER BY R.LASTUPDATEDDATE DESC ";
		} else {
			sql = "select L.PERMITNUMBER from IOP.JSA_LOCATION as L inner join "
					+ " IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER inner join"
					+ " IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER " + " where L.FACILITY = '" + facility
					+ "'" + " AND (J.ISACTIVE = 1 or J.ISACTIVE = 2) ORDER BY R.LASTUPDATEDDATE DESC ";
		}
		System.out.println("sql : " + sql);
		try {
			Query q = getSession().createNativeQuery(sql);
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(sql);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<JsaDetailsDto> getJsaByLocation(String muwi, String facility) {
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
		System.out.println(q.getResultList().toArray());
		List<Object[]> obj = q.getResultList();
		logger.info(obj.toString());
		List<JsaRecord> jsaList = null;
		int objLength = obj.size();
		for (int i = 0; i < objLength; i++) {
			Object[] rs = obj.get(i);
			JsaRecord jsaDto = new JsaRecord();
			jsaDto.setJsaPermitNumber((String) rs[1]);
			jsaDto.setTaskDescription((String) rs[2]);
			jsaDto.setStatus((String) rs[3]);
			jsaDto.setPtwPermitNumber((String) rs[4]);
			jsaDto.setCreatedDate(null);
			jsaDto.setCreatedBy((String) rs[6]);
			jsaDto.setFacilityOrSite((String) rs[7]);
			jsaDto.setLastUpdatedDate(null);
			jsaDto.setApprovedDate(null);
			jsaDto.setPermitNumber((Integer) rs[10]);
			jsaList.add(jsaDto);
		}
		System.out.println(jsaList.toString());
		logger.info(jsaList.toString());
		List<JsaDetailsDto> jsaDetailsDtoList = null;
		for (JsaRecord jDto : jsaList) {
			JsaDetailsDto temp = new JsaDetailsDto();
			List<JsaDetailsDto> res = jsaDetailsDtoList.stream()
					.filter(c -> c.getJsaPermitNumber().equalsIgnoreCase(jDto.getJsaPermitNumber()))
					.collect(Collectors.toList());
			if (res.get(0).getJsaPermitNumber().equals((jDto.getJsaPermitNumber()))) {
				res.get(0).getPtwPermitNumber().add(jDto.getPtwPermitNumber());
				continue;
			} else {
				temp.setPermitNumber(jDto.getPermitNumber());
			}
			temp.setApprovedDate(jDto.getApprovedDate());
			temp.setCreatedBy(jDto.getCreatedBy());
			temp.setCreatedDate(jDto.getCreatedDate());
			temp.setFacilityOrSite(Arrays.asList(jDto.getFacilityOrSite()));
			temp.setJsaPermitNumber(jDto.getJsaPermitNumber());
			temp.setLastUpdatedDate(jDto.getLastUpdatedDate());
			temp.setPermitNumber(jDto.getPermitNumber());
			temp.setStatus(jDto.getStatus());
			temp.setTaskDescription(jDto.getTaskDescription());
			jsaDetailsDtoList.add(temp);
		}
		logger.info(jsaDetailsDtoList.toString());
		System.out.println(jsaDetailsDtoList.toString());
		return jsaDetailsDtoList;
	}
	/*
	 * @SuppressWarnings("unchecked") public List<JsaDetailsDto>
	 * getJsaByLocations(String muwi, String facility) { List<String>
	 * permitNumberList = getPermitNumberList(muwi, facility); String sql =
	 * "select J.JSAPERMITNUMBER, J.TASKDESCRIPTION,J.STATUS,P.PTWPERMITNUMBER, "
	 * + "R.CREATEDDATE,R.CREATEDBY , L.FACILTYORSITE, R.LASTUPDATEDDATE, " +
	 * "R.APPROVEDDATE,J.PERMITNUMBER from IOP.JSA_LOCATION as L inner join " +
	 * "IOP.JSAHEADER as J on L.PERMITNUMBER = J.PERMITNUMBER " +
	 * "left join IOP.PTWHEADER as P on L.PERMITNUMBER = P.PERMITNUMBER " +
	 * "inner join IOP.JSAREVIEW as R on L.PERMITNUMBER = R.PERMITNUMBER " +
	 * "where J.PERMITNUMBER IN (?) ORDER BY R.LASTUPDATEDDATE DESC ";
	 * Connection connection = ((SessionImpl) getSession()).connection();
	 * PreparedStatement ps=connection.prepareStatement(sql); ps.setObject(1,)
	 * ResultSet rs=ps.executeQuery(); List<Object> obj = q.getResultList();
	 * logger.info(obj.toString()); List<JsaRecord> jsaList = null; int
	 * objLength = obj.size(); for (int i = 0; i < objLength; i++) {
	 * List<Object> rs = (List<Object>) obj.get(i); JsaRecord jsaDto = new
	 * JsaRecord(); jsaDto.setJsaPermitNumber((String) rs.get(1));
	 * jsaDto.setTaskDescription((String) rs.get(2)); jsaDto.setStatus((String)
	 * rs.get(3)); jsaDto.setPtwPermitNumber((String) rs.get(4));
	 * jsaDto.setCreatedDate((Date) rs.get(5)); jsaDto.setCreatedBy((String)
	 * rs.get(6)); jsaDto.setFacilityOrSite((String) rs.get(7));
	 * jsaDto.setLastUpdatedDate((Date) rs.get(8));
	 * jsaDto.setApprovedDate((Date) rs.get(9));
	 * jsaDto.setPermitNumber((Integer) rs.get(10)); jsaList.add(jsaDto); }
	 * logger.info(jsaList.toString()); List<JsaDetailsDto> jsaDetailsDtoList =
	 * null; for (JsaRecord jDto : jsaList) { JsaDetailsDto temp = new
	 * JsaDetailsDto(); List<JsaDetailsDto> res = jsaDetailsDtoList.stream()
	 * .filter(c ->
	 * c.getJsaPermitNumber().equalsIgnoreCase(jDto.getJsaPermitNumber()))
	 * .collect(Collectors.toList()); if
	 * (res.get(0).getJsaPermitNumber().equals((jDto.getJsaPermitNumber()))) {
	 * res.get(0).getPtwPermitNumber().add(jDto.getPtwPermitNumber()); continue;
	 * } else { temp.setPermitNumber(jDto.getPermitNumber()); }
	 * temp.setApprovedDate(jDto.getApprovedDate());
	 * temp.setCreatedBy(jDto.getCreatedBy());
	 * temp.setCreatedDate(jDto.getCreatedDate());
	 * temp.setFacilityOrSite(Arrays.asList(jDto.getFacilityOrSite()));
	 * temp.setJsaPermitNumber(jDto.getJsaPermitNumber());
	 * temp.setLastUpdatedDate(jDto.getLastUpdatedDate());
	 * temp.setPermitNumber(jDto.getPermitNumber());
	 * temp.setStatus(jDto.getStatus());
	 * temp.setTaskDescription(jDto.getTaskDescription());
	 * jsaDetailsDtoList.add(temp); } logger.info(jsaDetailsDtoList.toString());
	 * return jsaDetailsDtoList; }
	 */
}
