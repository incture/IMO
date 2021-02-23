package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.reports.PMCReportBaseDto;


public class DOPVarianceReportResponseDto extends PMCReportBaseDto {

	private List<DOPVarianceDto> tasks;
	private ResponseMessage message;
	private ResponseMessage responseMessage;
	
	
	public List<DOPVarianceDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<DOPVarianceDto> tasks) {
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
		return "DOPVarianceReportResponseDto [tasks=" + tasks + ", message=" + message + ", responseMessage="
				+ responseMessage + "]";
	}

	

}
