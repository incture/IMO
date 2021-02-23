package com.murphy.taskmgmt.dto;

import java.util.List;

public class WorkBenchResponseDto {
	
	private List<WorkBenchDto> WorkBenchDtoList;
	private ResponseMessage responseMessage;
	/**
	 * @return the workBenchDtoList
	 */
	public List<WorkBenchDto> getWorkBenchDtoList() {
		return WorkBenchDtoList;
	}
	/**
	 * @param workBenchDtoList the workBenchDtoList to set
	 */
	public void setWorkBenchDtoList(List<WorkBenchDto> workBenchDtoList) {
		WorkBenchDtoList = workBenchDtoList;
	}
	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	

}
