package com.murphy.taskmgmt.dto;

import java.util.List;

public class FracWellStatusResponseDto {
	private List<FracWellStatusDto> fracWellStatusDtoList;
	private ResponseMessage message;

	public List<FracWellStatusDto> getFracWellStatusDtoList() {
		return fracWellStatusDtoList;
	}

	public void setFracWellStatusDtoList(List<FracWellStatusDto> fracWellStatusDtoList) {
		this.fracWellStatusDtoList = fracWellStatusDtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
