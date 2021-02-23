package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import org.json.JSONObject;

import com.murphy.taskmgmt.dto.ActivityLogDto;
import com.murphy.taskmgmt.dto.EIFormDto;
import com.murphy.taskmgmt.dto.EIFormListResponseDto;
import com.murphy.taskmgmt.dto.EnergyIsolationDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface EnergyIsolationFacadeLocal {
	ResponseMessage createForm(EIFormDto dto);

	EIFormListResponseDto getFormsByLocation(String locationCode, String locationType, String monthTime, int weekTime, 
			int page, int page_size,boolean isActive);

	EIFormDto getFormById(String formId);

	List<String> getContractorList();
	
	List<String> getShiftList();

	List<String> getReasonList();

	ResponseMessage updateActivityStatus(String id, String value);

	ResponseMessage createActivity(ActivityLogDto activity);

	void pushDataForNotification();
	
	String deleteLock(String id);

//	JSONObject generateEIPdfTemplate(EnergyIsolationDto dto, String pdfTemplateUrl, String logoUrl, String tickImageUrl);

	ResponseMessage sendEmail(String formId, String affectedPersonnelIdList);

	String generateEIPdfTemplate(EnergyIsolationDto dto);

}
