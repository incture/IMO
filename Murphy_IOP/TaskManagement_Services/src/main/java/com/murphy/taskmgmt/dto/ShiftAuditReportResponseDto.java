package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.reports.PMCReportBaseDto;

public class ShiftAuditReportResponseDto extends PMCReportBaseDto {

	
	private List<ShiftAuditLogDto> tasks;
	private ResponseMessage message;
	private ResponseMessage responseMessage;
	
	public List<ShiftAuditLogDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<ShiftAuditLogDto> tasks) {
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
	@Override
	public String toString() {
		return "ShiftAuditReportResponseDto [tasks=" + tasks + ", message=" + message + ", responseMessage="
				+ responseMessage + "]";
	}
	
	

}
