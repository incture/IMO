package com.murphy.taskmgmt.dto;

import java.util.List;

public class DowntimeWellChildCodeResponseDto {

	private List<DowntimeWellChildCodeDto> downtimeWellChildCodeDtoList;
	private ResponseMessage message;

	public List<DowntimeWellChildCodeDto> getDowntimeWellChildCodeDtoList() {
		return downtimeWellChildCodeDtoList;
	}

	public void setDowntimeWellChildCodeDtoList(List<DowntimeWellChildCodeDto> downtimeWellChildCodeDtoList) {
		this.downtimeWellChildCodeDtoList = downtimeWellChildCodeDtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

}
