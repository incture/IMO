package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.DOPResponseDto;
import com.murphy.taskmgmt.dto.DopDummyDto;

//these classes are no longer used
public interface DopAutomationFacadeLocal {

	public List<DopDummyDto> fetchVarianceData();
	
	public DOPResponseDto dgpQueryForOtherDetails(String locationCodesList);
}
