package com.murphy.taskmgmt.dto;

import java.util.List;

public class PiggingUIReqDto {

	List<PiggingSchedulerDto> piggingSchedulerDtoList;

	public List<PiggingSchedulerDto> getPiggingSchedulerDtoList() {
		return piggingSchedulerDtoList;
	}

	public void setPiggingSchedulerDtoList(List<PiggingSchedulerDto> piggingSchedulerDtoList) {
		this.piggingSchedulerDtoList = piggingSchedulerDtoList;
	}

	@Override
	public String toString() {
		return "PiggingUIReqDto [piggingSchedulerDtoList=" + piggingSchedulerDtoList + "]";
	}
	
}
