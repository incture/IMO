package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class AuditDto extends BaseDto{

	private String auditId;
	private String taskId;
	private Date createdAt;
	private String action;
	private String actionBy;
	

	


	@Override
	public String toString() {
		return "AuditDo [auditId=" + auditId + ", taskId=" + taskId + ", createdAt=" + createdAt + ", action=" + action
				+ ", actionBy=" + actionBy + "]";
	}





	public String getAuditId() {
		return auditId;
	}





	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}





	public String getTaskId() {
		return taskId;
	}





	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}





	public Date getCreatedAt() {
		return createdAt;
	}





	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}





	public String getAction() {
		return action;
	}





	public void setAction(String action) {
		this.action = action;
	}





	public String getActionBy() {
		return actionBy;
	}





	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
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
