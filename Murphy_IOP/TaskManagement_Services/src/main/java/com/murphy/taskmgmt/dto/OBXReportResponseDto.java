package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.reports.PMCReportBaseDto;

public class OBXReportResponseDto extends PMCReportBaseDto {

	private List<ObxTaskAllocationDto> tasks;
	@Override
	public String toString() {
		return "OBXReportResponseDto [tasks=" + tasks + ", message=" + message + ", responseMessage=" + responseMessage
				+ "]";
	}
	public List<ObxTaskAllocationDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<ObxTaskAllocationDto> tasks) {
		this.tasks = tasks;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	private ResponseMessage message;
	private ResponseMessage responseMessage;

}
