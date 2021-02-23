package com.murphy.taskmgmt.dto;

import java.util.List;

public class TaskSchedulingResponseDto {

	private ResponseMessage responseMessage;
	private List<TaskSchedulingUserDto> taskList;
	private List<TaskSchedulingGraphDto> graphDtos;

	public List<TaskSchedulingGraphDto> getGraphDtos() {
		return graphDtos;
	}

	public void setGraphDtos(List<TaskSchedulingGraphDto> graphDtos) {
		this.graphDtos = graphDtos;
	}

	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<TaskSchedulingUserDto> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskSchedulingUserDto> taskList) {
		this.taskList = taskList;
	}

	@Override
	public String toString() {
		return "TaskSchedulingResponseDto [responseMessage=" + responseMessage + ", taskList=" + taskList
				+ ", graphDtos=" + graphDtos + "]";
	}

}
