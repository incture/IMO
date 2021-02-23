package com.murphy.taskmgmt.dto;

public class DOPResponseDto {
	
	private String taskId;
	private ResponseMessage responseMessage;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	

}
