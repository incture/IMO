package com.murphy.taskmgmt.service.interfaces;

import com.murphy.integration.dto.UIResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface AlsStagingFacadeLocal {
	
	public ResponseMessage stageALSData(String muwi);
	
	public ResponseMessage removeALSData(String muwi);

	UIResponseDto getInvestigationDetails(String m);

}
