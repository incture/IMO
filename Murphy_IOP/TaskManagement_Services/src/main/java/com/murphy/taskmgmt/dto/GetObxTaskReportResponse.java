package com.murphy.taskmgmt.dto;

import java.util.List;

public class GetObxTaskReportResponse {
	private ResponseMessage responseMessage;
	private List<ObxTaskAllocationDto> ObxTaskAllocationDto;
	@Override
	public String toString() {
		return "GetObxTaskReportResponse [responseMessage=" + responseMessage + ", ObxTaskAllocationDto="
				+ ObxTaskAllocationDto + "]";
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<ObxTaskAllocationDto> getObxTaskAllocationDto() {
		return ObxTaskAllocationDto;
	}
	public void setObxTaskAllocationDto(List<ObxTaskAllocationDto> obxTaskAllocationDto) {
		ObxTaskAllocationDto = obxTaskAllocationDto;
	}
}
