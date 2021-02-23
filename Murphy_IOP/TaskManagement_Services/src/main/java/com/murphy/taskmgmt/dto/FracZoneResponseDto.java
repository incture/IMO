package com.murphy.taskmgmt.dto;

import java.util.List;

public class FracZoneResponseDto {
	private List<FracZoneDto> fracZoneDtoList;
	private ResponseMessage message;

	public List<FracZoneDto> getFracZoneDtoList() {
		return fracZoneDtoList;
	}

	public void setFracZoneDtoList(List<FracZoneDto> fracZoneDtoList) {
		this.fracZoneDtoList = fracZoneDtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
