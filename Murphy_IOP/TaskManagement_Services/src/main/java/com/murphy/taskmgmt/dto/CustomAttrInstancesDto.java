package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CustomAttrInstancesDto extends BaseDto {


	private String clId;	
	private String taskId;
	private String value;
	private String attrId;


	@Override
	public String toString() {
		return "CheckListInstancesDto [clId=" + clId + ", taskId=" + taskId + ", attrId=" + attrId + ", value=" + value+ "]";
	}


	public String getClId() {
		return clId;
	}


	public void setClId(String clId) {
		this.clId = clId;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
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


	public String getAttrId() {
		return attrId;
	}


	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}






}
