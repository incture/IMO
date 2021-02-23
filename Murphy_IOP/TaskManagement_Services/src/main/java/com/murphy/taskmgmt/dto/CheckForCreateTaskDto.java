package com.murphy.taskmgmt.dto;

import java.util.List;

public class CheckForCreateTaskDto {
	
	private List<TaskListDto>  taskList;
	private ResponseMessage responseMessage;
	private boolean canCreate;
	
	public List<TaskListDto> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<TaskListDto> taskList) {
		this.taskList = taskList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public boolean isCanCreate() {
		return canCreate;
	}
	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	@Override
	public String toString() {
		return "CheckForCreateTaskDto [taskList=" + taskList + ", responseMessage=" + responseMessage + ", canCreate="
				+ canCreate + "]";
	}

}
