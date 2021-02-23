package com.murphy.taskmgmt.reports;

import java.util.List;

import com.murphy.taskmgmt.dto.ObxTaskDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public class ObxTasksReportDto extends PMCReportBaseDto{
	private List<ObxTaskDto> tasks;
	private ResponseMessage message;
	private ResponseMessage responseMessage;
	public List<ObxTaskDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<ObxTaskDto> tasks) {
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
		return "ObxTasksReportDto [tasks=" + tasks + ", message=" + message + ", responseMessage=" + responseMessage
				+ "]";
	}
	
}
