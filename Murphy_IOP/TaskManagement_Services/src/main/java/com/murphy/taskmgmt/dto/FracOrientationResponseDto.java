package com.murphy.taskmgmt.dto;

import java.util.List;

public class FracOrientationResponseDto {

	private List<FracOrientationDto> fracOrientationDtoList;
	private ResponseMessage message;

	public List<FracOrientationDto> getFracOrientationDtoList() {
		return fracOrientationDtoList;
	}

	public void setFracOrientationDtoList(List<FracOrientationDto> fracOrientationDtoList) {
		this.fracOrientationDtoList = fracOrientationDtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
