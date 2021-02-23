package com.murphy.taskmgmt.dto;

import java.util.List;

public class NDVTaskListResponseDto {
	
	private List<NDVTaskListDto> taskList;
	private ResponseMessage responseMessage;
	private int potTaskCount;
	private int alsTaskCount;
	private int engTaskCount;
	
	public List<NDVTaskListDto> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<NDVTaskListDto> taskList) {
		this.taskList = taskList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public int getPotTaskCount() {
		return potTaskCount;
	}
	public void setPotTaskCount(int potTaskCount) {
		this.potTaskCount = potTaskCount;
	}
	public int getAlsTaskCount() {
		return alsTaskCount;
	}
	public void setAlsTaskCount(int alsTaskCount) {
		this.alsTaskCount = alsTaskCount;
	}
	public int getEngTaskCount() {
		return engTaskCount;
	}
	public void setEngTaskCount(int engTaskCount) {
		this.engTaskCount = engTaskCount;
	}
	@Override
	public String toString() {
		return "NDVTaskListResponseDto [taskList=" + taskList + ", responseMessage=" + responseMessage
				+ ", potTaskCount=" + potTaskCount + ", alsTaskCount=" + alsTaskCount + ", engTaskCount=" + engTaskCount
				+ "]";
	}
	
}
