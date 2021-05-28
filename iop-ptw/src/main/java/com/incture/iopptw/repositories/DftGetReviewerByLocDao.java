package com.incture.iopptw.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.DftGetReviewerPayloadDto;
import com.incture.iopptw.dtos.DftGetReviewerResponseDto;

@Repository("DftGetReviewerByLocDao")
public class DftGetReviewerByLocDao extends BaseDao{
	
	public List<DftGetReviewerResponseDto> getReviewerByLoc(DftGetReviewerPayloadDto d){
		String department = d.getDepartment();
		String field = d.getFieldCode();
		String facility = d.getFacilityCode();
		String wellpad = d.getWellPadCode();
		String well = d.getWellCode();
		String query,wellquery,fieldquery,finalquery = "",wellpadquery,facilityquery;
		List<DftGetReviewerResponseDto> finalData = new ArrayList<DftGetReviewerResponseDto>();
		try{
			query = "select * from IOP.DFT_REVIEWERS where department like '%"+department+"%'";
			if(well !="" && well != null){
				
				wellquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = '" + facility + "' and wellpad_code = '" + wellpad 
						+ "' and well_code = '" + well + "'";
				
				wellpadquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field
						+ "' and facility_code = '" + facility + "' and wellpad_code = '"+ wellpad + "' and well_code = ''";
				
				facilityquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = '" + facility + "' and wellpad_code = ''" + " and well_code = ''";
				fieldquery = " select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = ''" + " and wellpad_code = ''" + " and well_code = ''";
				finalquery = query + " and puser_id in (" + fieldquery + facilityquery + wellpadquery + wellquery + ")";
				System.out.println("finalquery : 1 " + finalquery);
			}
			else if((well == "" || well == null)&& (wellpad != "" && wellpad != null)){
				wellpadquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code ='" + field 
						+ "' and facility_code = '" +facility +"' and wellpad_code = '" + wellpad 
						+ "' and well_code = ''";
				
				facilityquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = '" + facility + "' and wellpad_code = ''"  + " and well_code = ''";
				fieldquery = " select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field  
						+"' and facility_code = ''" + " and wellpad_code = ''" + " and well_code = ''";
				finalquery = query + " and puser_id in (" + fieldquery + facilityquery + wellpadquery + ")";
				System.out.println("finalquery : 2 " + finalquery);
			}
			
			else if((well == "" || well == null) && (wellpad == "" || wellpad == null) && (facility != "" || facility == null)){
				
				facilityquery = " union select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = '" + facility + "' and wellpad_code = ''" + " and well_code = ''";
				fieldquery = "select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code = '" + field 
						+ "' and facility_code = ''" + " and wellpad_code = ''" + " and well_code = ''";
				finalquery = query + " and puser_id in (" + fieldquery + facilityquery + ")";
				System.out.println("finalquery : 3 " + finalquery);
			}
			
			else if((well == null || well == "") && (wellpad == "" || wellpad == null)  && (facility == "" || facility == null) 
					&& (field != "" && field != null)){
				fieldquery = " select PUSER_ID from IOP.DFT_REVIEWER_LOCATION where field_code ='" + field 
						+ "' and facility_code = ''" + " and wellpad_code = ''" + " and well_code = ''";
				finalquery = query + " and PUSER_ID in ("+ fieldquery + ")";
				System.out.println("finalquery : 4 " + finalquery);
			}
			Query q = getSession().createNativeQuery(finalquery);
			@SuppressWarnings("unchecked")
			List<Object[]> obj = q.getResultList();
			for(Object[] a : obj){
				DftGetReviewerResponseDto temp = new DftGetReviewerResponseDto();
				temp.setPUserId((String)a[0]);
				temp.setFirstName((String)a[1]);
				temp.setLastName((String)a[2]);
				temp.setFullName(temp.getFirstName() + " "+ temp.getLastName());
				temp.setEmailID((String)a[3]);
				temp.setDepartment((String)a[4]);
				temp.setRole((String)a[5]);
				finalData.add(temp);
			}
			return finalData;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
