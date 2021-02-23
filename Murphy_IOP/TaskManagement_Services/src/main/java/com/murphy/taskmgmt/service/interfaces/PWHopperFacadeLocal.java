package com.murphy.taskmgmt.service.interfaces;

import com.murphy.integration.dto.UIRequestDto;
import com.murphy.taskmgmt.dto.CheckListResponseDto;
import com.murphy.taskmgmt.dto.PWHopperResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface PWHopperFacadeLocal {

	CheckListResponseDto getCheckList(String userType, String locationCode ,String investigationId);

//	ResponseMessage saveOrUpdateInvestInsts(CheckListResponseDto requestDto);
	
	ResponseMessage removeProactive(String locationCode);

	PWHopperResponseDto getpwHopperList(UIRequestDto requestDto);

	String setDataForPWHopperWell();

	ResponseMessage saveOrUpdateInvestInsts(CheckListResponseDto requestDto, boolean isProactive);
	
}
