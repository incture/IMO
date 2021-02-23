package com.murphy.taskmgmt.dto;

import java.util.List;

public class RootCauseResponseDto {

	private List<RootCauseDto> rootCauseList;
	private ResponseMessage responseMessage;

	public List<RootCauseDto> getRootCauseList() {
		return rootCauseList;
	}

	public void setRootCauseList(List<RootCauseDto> rootCauseList) {
		this.rootCauseList = rootCauseList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "RootCauseResponseDto [rootCauseList=" + rootCauseList + ", responseMessage=" + responseMessage + "]";
	}

}
