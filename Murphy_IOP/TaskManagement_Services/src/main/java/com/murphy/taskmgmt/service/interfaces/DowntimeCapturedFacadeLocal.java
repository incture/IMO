package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.DowntimeWellChildCodeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface DowntimeCapturedFacadeLocal {

	
	//ResponseMessage createDowntimeCaptured(DowntimeCapturedDto dto);

//	ResponseMessage updateDowntimeCaptured(DowntimeCapturedDto dto);

	//DowntimeResponseDto getAllDowntimeCaptured(String type);

	DowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet);

	ResponseMessage updateDowntimeCaptured(DowntimeUpdateDto downtimeDto);

	ResponseMessage createDowntimeCaptured(DowntimeUpdateDto dto);
	
	//added as a part of Data Maintenance - sprint 5
		//SOC
	public DowntimeWellParentCodeResponseDto getActiveParenCodeForWellDowntime(String country);
	
	public DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntime();
	
	public DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntimeByParentCode(String parentCode);
	//EOC
	
	//added for location history of downtime for canada muwis
	DowntimeResponseDto getDowntimeHierarchyForCanada(DowntimeRequestDto dtoGet);

	
}
