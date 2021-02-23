package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class NonDispatchResponseDto extends BaseDto {

	private List<NonDispatchTaskDto> taskList;	
	private ResponseMessage responseMessage;
	
	
	
	public List<NonDispatchTaskDto> getTaskList() {
		return taskList;
	}



	public void setTaskList(List<NonDispatchTaskDto> taskList) {
		this.taskList = taskList;
	}



	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}



	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}



	@Override
	public String toString() {
		return "NonDispatchResponseDto [taskList=" + taskList + ", responseMessage=" + responseMessage + "]";
	}



	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}



}
