package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.reports.PMCReportBaseDto;


public class AuditReportResponseDto extends PMCReportBaseDto {

	private List<AuditReportDto> tasks;
	private ResponseMessage message;
	private ResponseMessage responseMessage;

	

	public List<AuditReportDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<AuditReportDto> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "AuditReportResponseDto [tasks=" + tasks + ", message=" + message + "]";
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


}
