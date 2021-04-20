package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.PermitsByLocDataDto;
import com.incture.ptw.dto.PermitsByLocInnerDto;
import com.incture.ptw.dto.PermitsByLocPayloadDto;
import com.incture.ptw.dto.PermitsByLocRecordDto;

@Repository("PermitsByLocDao")
public class PermitsByLocDao extends BaseDao {
	@Autowired
	private JsaByLocationDao jsaByLocationDao;

	@SuppressWarnings("unchecked")
	public PermitsByLocPayloadDto getPermitsByLoc(String muwi, String facility) {

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
		List<Object[]> sqlData = q.getResultList();
		List<PermitsByLocRecordDto> res = new ArrayList<PermitsByLocRecordDto>();
		for (Object[] d : sqlData) {
			PermitsByLocRecordDto temp = new PermitsByLocRecordDto();
			temp.setJsaPermitNumber((String) d[0]);
			temp.setPtwPermitNumber((String) d[1]);
			temp.setCreatedBy((String) d[2]);
			temp.setIsCwp((Byte) d[3]);
			temp.setIsHwp((Byte) d[4]);
			temp.setIsCse((Byte) d[5]);
			temp.setCreatedDate((Date) d[6]);
			temp.setFacilityorsite((String) d[7]);
			temp.setPermitNumber(String.valueOf(d[8]));
			temp.setLastUpdatedDate((Date) d[9]);
			temp.setStatus((String) d[10]);
			res.add(temp);
			logger.info("PermitsByLocDao | getPermitsByLoc | temp " + temp.getCreatedBy() + temp.getFacilityorsite()
					+ temp.getJsaPermitNumber() + temp.getPermitNumber() + temp.getPtwPermitNumber() + temp.getStatus()
					+ temp.getIsCse() + temp.getIsCwp() + temp.getIsHwp());
		}
		logger.info("PermitsByLocDao | getPermitsByLoc | res  " + res);
		PermitsByLocDataDto obj = new PermitsByLocDataDto();
		List<PermitsByLocDataDto> rawData = new ArrayList<PermitsByLocDataDto>();
		for (int i = 0; i < res.size(); i++) {
			obj.setJsaPermitNumber(res.get(i).getJsaPermitNumber());
			obj.setPtwPermitNumber(res.get(i).getPtwPermitNumber());
			obj.setCreatedBy(res.get(i).getCreatedBy());
			obj.setCreatedDate(res.get(i).getCreatedDate());
			obj.setIsCwp(res.get(i).getIsCwp());
			obj.setIsHwp(res.get(i).getIsHwp());
			obj.setIsCse(res.get(i).getIsCse());
			obj.setPermitNumber(res.get(i).getPermitNumber());
			obj.setLastUpdatedDate(res.get(i).getLastUpdatedDate());
			obj.setStatus(res.get(i).getStatus());

			if (obj.getFacilityorsite() == null) {
				obj.setFacilityorsite(new ArrayList<String>());
			}

			if (i == (res.size() - 1)) {
				if (res.get(i).getFacilityorsite() != null) {
					List<String> str = obj.getFacilityorsite();
					str.add(res.get(i).getFacilityorsite());
					obj.setFacilityorsite(str);
					rawData.add(obj);
					break;
				}
			} else if (res.get(i).getPtwPermitNumber() == res.get(i + 1).getPtwPermitNumber()) {
				if (res.get(i).getFacilityorsite() != null) {
					List<String> str = obj.getFacilityorsite();
					str.add(res.get(i).getFacilityorsite());
					obj.setFacilityorsite(str);
				}
			} else {
				if (res.get(i).getFacilityorsite() != null) {
					List<String> str = obj.getFacilityorsite();
					str.add(res.get(i).getFacilityorsite());
					obj.setFacilityorsite(str);
				}
				rawData.add(obj);
				obj = new PermitsByLocDataDto();
				obj.setFacilityorsite(new ArrayList<String>());
			}

		}
		logger.info("PermitsByLocDao | getPermitsByLoc | rawData  " + rawData);
		PermitsByLocInnerDto permitRecord = new PermitsByLocInnerDto();
		PermitsByLocPayloadDto output = new PermitsByLocPayloadDto();
		
		for (int i = 0; i < rawData.size(); i++) {
			permitRecord.setJsaPermitNumber(rawData.get(i).getJsaPermitNumber());
			permitRecord.setPtwPermitNumber(rawData.get(i).getPtwPermitNumber());
			permitRecord.setCreatedBy(rawData.get(i).getCreatedBy());
//			isCwp = rawData.get(i).getIsCwp();
//			isHwp = rawData.get(i).getIsHwp();
//			isCse = rawData.get(i).getIsCse();
			Byte isCwp = 0, isHwp = 0, isCse = 0;
			if(rawData.get(i).getIsCwp() != null){
				isCwp = rawData.get(i).getIsCwp();
			}
			if(rawData.get(i).getIsHwp() != null){
				isHwp = rawData.get(i).getIsHwp();
			}
			if(rawData.get(i).getIsCse() != null){
				isCse = rawData.get(i).getIsCse();
			}
			permitRecord.setCreatedDate(rawData.get(i).getCreatedDate());
			permitRecord.setFacilityorsite(rawData.get(i).getFacilityorsite());
			permitRecord.setPermitNumber(rawData.get(i).getPermitNumber());
			permitRecord.setLastUpdatedDate(rawData.get(i).getLastUpdatedDate());
			permitRecord.setStatus(rawData.get(i).getStatus());
			logger.info("PermitsByLocDao | getPermitsByLoc | permitRecord  " + permitRecord.toString());
			logger.info("PermitsByLocDao | getPermitsByLoc | isCwp  " + rawData.get(i).getIsCwp() + "isHwp"
					+ rawData.get(i).getIsHwp() + "isCse" + rawData.get(i).getIsCse());
			if (isCwp == 1) {
				logger.info("PermitsByLocDao | getPermitsByLoc | isCwp ==1  " + output.getCWP());
				if(output.getCWP() == null){
					List<PermitsByLocInnerDto> cwpData = new ArrayList<PermitsByLocInnerDto>();
					cwpData.add(permitRecord);
					output.setCWP(cwpData);
				}else{
					List<PermitsByLocInnerDto> cwpData = output.getCWP();
					cwpData.add(permitRecord);
					output.setCWP(cwpData);
				}
				
				logger.info("PermitsByLocDao | getPermitsByLoc | output1  " + output);
			}
			if (isHwp == 1) {
				logger.info("PermitsByLocDao | getPermitsByLoc | isHwp ==1  " + output.getHWP());
				if(output.getHWP() == null){
					List<PermitsByLocInnerDto> hwpData = new ArrayList<PermitsByLocInnerDto>();
					hwpData.add(permitRecord);
					output.setHWP(hwpData);
				}else{
					List<PermitsByLocInnerDto> hwpData = output.getHWP();
					hwpData.add(permitRecord);
					output.setHWP(hwpData);
				}
				
				logger.info("PermitsByLocDao | getPermitsByLoc | output1  " + output);
			}
			if (isCse == 1) {
				logger.info("PermitsByLocDao | getPermitsByLoc | isCse ==1  " + output.getCSE());
				if(output.getCSE() == null){
					List<PermitsByLocInnerDto> cseData = new ArrayList<PermitsByLocInnerDto>();
					cseData.add(permitRecord);
					output.setCSE(cseData);
				}else{
					List<PermitsByLocInnerDto> cseData = output.getCSE();
					cseData.add(permitRecord);
					output.setCSE(cseData);
				}
				
				logger.info("PermitsByLocDao | getPermitsByLoc | output1  " + output);
			}
			permitRecord = new PermitsByLocInnerDto();
		}
		logger.info("PermitsByLocDao | getPermitsByLoc | Finaloutput  " + output);
		return output;
	}

}
