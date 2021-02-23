package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.CanaryStagingDto;
import com.murphy.taskmgmt.dto.FracDropDownResponseDto;
import com.murphy.taskmgmt.dto.FracHitDto;
import com.murphy.taskmgmt.dto.FracOrientationResponseDto;
import com.murphy.taskmgmt.dto.FracPackEngViewResponseDto;
import com.murphy.taskmgmt.dto.FracScenarioDto;
import com.murphy.taskmgmt.dto.FracScenarioLookUpResponseDto;
import com.murphy.taskmgmt.dto.FracWellStatusResponseDto;
import com.murphy.taskmgmt.dto.FracZoneResponseDto;
import com.murphy.taskmgmt.dto.OffsetFracPackDto;
import com.murphy.taskmgmt.dto.OffsetFracPackRequestDto;
import com.murphy.taskmgmt.dto.OffsetFracPackResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;


public interface OffsetFracPackFacadeLocal {

	
	//ResponseMessage updateFacPack(OffsetFracPackDto dto);
	OffsetFracPackResponseDto getFracPack(String fracId,String fieldCode, String wellcode);
	OffsetFracPackResponseDto getFackPacksByField(String fieldCode);
	ResponseMessage createFracPack(OffsetFracPackRequestDto dto);
	OffsetFracPackRequestDto getBoed(OffsetFracPackRequestDto dto);
	FracPackEngViewResponseDto getEngView(String userRole);
	ResponseMessage insertFractHit(FracHitDto dto);
	List<CanaryStagingDto> getActiveValues(List<String> muwis);
	ResponseMessage markCompleteFracPack(List<FracHitDto> dtos);
	ResponseMessage changeWellStatus(List<FracHitDto> dtos);
	FracHitDto getFracHit(long fracId, String muwiId);
	
	//Adding for incident INC0078316
	List<FracScenarioDto> getFracScenario();
	
	//added as a part of data maintenance- sprint 5 
	//SOC
	public FracScenarioLookUpResponseDto getActiveFracScenarios();
	public FracWellStatusResponseDto getActiveFracWellStatus();
	public FracOrientationResponseDto getActiveFracOrientation();
	public FracZoneResponseDto getActiveFracZone();
	public FracDropDownResponseDto getAllActiveFracDropDowns();
	//EOC
	
	public ResponseMessage updateFracPack(List<OffsetFracPackDto> dtos);
	
}
