package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;


public class NDTaskMappingDto extends BaseDto {

	public NDTaskMappingDto() {
		super();
	}

	private String mappingId;	
	private String ndTaskId;
	private String taskId;
	private String status;


	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getNdTaskId() {
		return ndTaskId;
	}

	public void setNdTaskId(String ndTaskId) {
		this.ndTaskId = ndTaskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "NDTaskMappinDo [mappingId=" + mappingId + ", ndTaskId=" + ndTaskId + ", taskId=" + taskId + ", status="
				+ status + "]";
	}




}
