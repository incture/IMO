package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ObxTaskIdEndTimeListDto extends BaseDto{
	
	private String taskId;
	private Date endTime;
	
	public String getTaskId() {
		return taskId;
		
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
		return "ObxTaskIdEndTimeListDto [taskId=" + taskId + ", endTime=" + endTime + "]";
	}
	
	
	

}
