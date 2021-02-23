package com.murphy.taskmgmt.dto;

public class TaskDetailResponseDto {
	private TaskListDto taskListDto;
	private ResponseMessage ResponseMessage;
	
	public TaskListDto getTaskListDto() {
		return taskListDto;
	}
	public void setTaskListDto(TaskListDto taskListDto) {
		this.taskListDto = taskListDto;
	}
	public ResponseMessage getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		ResponseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "TaskDetailResponseDto [taskListDto=" + taskListDto + ", ResponseMessage=" + ResponseMessage + "]";
	}

}
