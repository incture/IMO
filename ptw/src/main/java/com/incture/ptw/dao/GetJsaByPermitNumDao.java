package com.incture.ptw.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.GetJsaByPermitNumPayloadDto;
import com.incture.ptw.dto.JsaheaderDto;

@Repository("GetJsaByPermitNumDao")
public class GetJsaByPermitNumDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public GetJsaByPermitNumPayloadDto getJsaByPermitNum(String permitNumber) {
		GetJsaByPermitNumPayloadDto getJsaByPermitNumPayloadDto = new GetJsaByPermitNumPayloadDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, JSAPERMITNUMBER,HASCWP,HASHWP, "
					+ " HASCSE,TASKDESCRIPTION,IDENTIFYMOSTSERIOUSPOTENTIALINJURY,ISACTIVE,STATUS from "
					+ " IOP.JSAHEADER where PERMITNUMBER = :permitNumber";
			logger.info("getActiveWorkers sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNumber", permitNumber);
			obj = q.getResultList();
			JsaheaderDto jsaheaderDto = new JsaheaderDto();
			for (Object[] a : obj) {
				jsaheaderDto.setPermitNumber((Integer) a[0]);
				jsaheaderDto.setJsaPermitNumber((String) a[1]);
				jsaheaderDto.setHasCwp(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasHwp(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasCse(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setTaskDescription((String) a[5]);
				jsaheaderDto.setIdentifyMostSeriousPotentialInjury((String) a[6]);
				jsaheaderDto.setIsActive(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setStatus((String) a[8]);
			}
			logger.info(jsaheaderDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHEADER(jsaheaderDto);
			logger.info("GetJsaByPermitNumDao | Final Output" + getJsaByPermitNumPayloadDto);
			return getJsaByPermitNumPayloadDto;
		} catch (Exception e) {
			logger.error("GetJsaByPermitNumDao | getJsaByPermitNum  error" + e.getMessage());
		}
		return null;

	}

}
