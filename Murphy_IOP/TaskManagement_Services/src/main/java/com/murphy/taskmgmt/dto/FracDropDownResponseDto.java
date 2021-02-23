package com.murphy.taskmgmt.dto;

public class FracDropDownResponseDto {
	private FracScenarioLookUpResponseDto fracScenarioLookUpResponseDto;
	private FracOrientationResponseDto fracOrientationResponseDto;
	private FracZoneResponseDto fracZoneResponseDto;
	private FracWellStatusResponseDto fracWellStatusResponseDto;
	private ResponseMessage message;
	
	public FracScenarioLookUpResponseDto getFracScenarioLookUpResponseDto() {
		return fracScenarioLookUpResponseDto;
	}

	public void setFracScenarioLookUpResponseDto(FracScenarioLookUpResponseDto fracScenarioLookUpResponseDto) {
		this.fracScenarioLookUpResponseDto = fracScenarioLookUpResponseDto;
	}

	public FracOrientationResponseDto getFracOrientationResponseDto() {
		return fracOrientationResponseDto;
	}

	public void setFracOrientationResponseDto(FracOrientationResponseDto fracOrientationResponseDto) {
		this.fracOrientationResponseDto = fracOrientationResponseDto;
	}

	public FracZoneResponseDto getFracZoneResponseDto() {
		return fracZoneResponseDto;
	}

	public void setFracZoneResponseDto(FracZoneResponseDto fracZoneResponseDto) {
		this.fracZoneResponseDto = fracZoneResponseDto;
	}

	public FracWellStatusResponseDto getFracWellStatusResponseDto() {
		return fracWellStatusResponseDto;
	}

	public void setFracWellStatusResponseDto(FracWellStatusResponseDto fracWellStatusResponseDto) {
		this.fracWellStatusResponseDto = fracWellStatusResponseDto;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	
}
