package com.incture.iopptw.repositories;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.JsaheaderDto;

@Repository
public class JsaHeaderDao extends BaseDao {
	public void insertJsaHeader(String permitNumber, JsaheaderDto jsaheaderDto) {
		try {
			String sql = "INSERT INTO IOP.JSAHEADER VALUES (?,?,?,?,?,?,?,?,?)";
			Query query = getSession().createNativeQuery(sql);
			logger.info("insertJsaHeader sql " + sql);
			query.setParameter(1, Integer.parseInt(permitNumber));
			query.setParameter(2, "JSA" + permitNumber);
			query.setParameter(3, jsaheaderDto.getHasCWP());
			query.setParameter(4, jsaheaderDto.getHasHWP());
			query.setParameter(5, jsaheaderDto.getHasCSE());
			query.setParameter(6, jsaheaderDto.getTaskDescription());
			query.setParameter(7, jsaheaderDto.getIdentifyMostSeriousPotentialInjury());
			query.setParameter(8, jsaheaderDto.getIsActive());
			query.setParameter(9, jsaheaderDto.getStatus());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void updateJsaHeader(String permitNumber, JsaheaderDto jsaheaderDto) {
		try {
			String sql = "UPDATE IOP.JSAHEADER SET STATUS =?, ISACTIVE=? where JSAPERMITNUMBER=? ";
			logger.info("updateJsaHeader sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaheaderDto.getStatus());
			query.setParameter(2, jsaheaderDto.getIsActive());
			query.setParameter(3, "JSA" + permitNumber);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void updateJsaHeaderByPermitNumber(JsaheaderDto jsaheaderDto) {
		try {
			String sql = "UPDATE IOP.JSAHEADER SET HASCWP=?,HASHWP=?,HASCSE=?,TASKDESCRIPTION=?,IDENTIFYMOSTSERIOUSPOTENTIALINJURY=?,ISACTIVE=?,STATUS=? WHERE PERMITNUMBER=?";
			logger.info("updateJsaHeaderByPermitNumber sql" + sql);
			Query query = getSession().createNativeQuery(sql);
			query.setParameter(1, jsaheaderDto.getHasCWP());
			query.setParameter(2, jsaheaderDto.getHasHWP());
			query.setParameter(3, jsaheaderDto.getHasCSE());
			query.setParameter(4, jsaheaderDto.getTaskDescription());
			query.setParameter(5, jsaheaderDto.getIdentifyMostSeriousPotentialInjury());
			query.setParameter(6, jsaheaderDto.getIsActive());
			query.setParameter(7, jsaheaderDto.getStatus());
			query.setParameter(8, jsaheaderDto.getPermitNumber());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	public JsaheaderDto getJsaHeader(String permitNumber){
		JsaheaderDto jsaheaderDto = new JsaheaderDto();
		List<Object[]> obj;
		try{
			String sql = "select distinct PERMITNUMBER, JSAPERMITNUMBER,HASCWP,HASHWP, "
					+ " HASCSE,TASKDESCRIPTION,IDENTIFYMOSTSERIOUSPOTENTIALINJURY,ISACTIVE,STATUS from "
					+ " IOP.JSAHEADER where PERMITNUMBER = :permitNumber";
			logger.info("JSAHEADER sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNumber", permitNumber);
			obj = q.getResultList();
			for (Object[] a : obj) {
				jsaheaderDto.setPermitNumber((Integer) a[0]);
				jsaheaderDto.setJsaPermitNumber((String) a[1]);
				jsaheaderDto.setHasCWP(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasHWP(Integer.parseInt(a[3].toString()));
				jsaheaderDto.setHasCSE(Integer.parseInt(a[4].toString()));
				jsaheaderDto.setTaskDescription((String) a[5]);
				jsaheaderDto.setIdentifyMostSeriousPotentialInjury((String) a[6]);
				jsaheaderDto.setIsActive(Integer.parseInt(a[7].toString()));
				jsaheaderDto.setStatus((String) a[8]);
			}
			logger.info(jsaheaderDto.toString());
			return jsaheaderDto;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
