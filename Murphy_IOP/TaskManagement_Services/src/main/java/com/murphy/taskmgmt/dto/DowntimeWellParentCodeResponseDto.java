package com.murphy.taskmgmt.dto;

import java.util.List;

public class DowntimeWellParentCodeResponseDto {

	private List<DowntimeWellParentCodeDto> downtimeWellParentCodeDtoList;
	private ResponseMessage message;

	public List<DowntimeWellParentCodeDto> getDowntimeWellParentCodeDtoList() {
		return downtimeWellParentCodeDtoList;
	}

	public void setDowntimeWellParentCodeDtoList(List<DowntimeWellParentCodeDto> downtimeWellParentCodeDtoList) {
		this.downtimeWellParentCodeDtoList = downtimeWellParentCodeDtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
