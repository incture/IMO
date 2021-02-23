package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ObxTaskUpdateDto extends BaseDto {
	
	private String taskId;
	private String processId;
	private String locationCode;
	private String assignUserEmail;
	private String userUpdatedBy;
	private Date userUpdatedAt;
	private String status;
	private String dispatchTaskEmail;
	private String userGroup;
	private List<ObxTaskIdEndTimeListDto> obxTaskIdEndTimeList;
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getProcessId() {
		return processId;
	}
	
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	public String getLocationCode() {
		return locationCode;
	}
	
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	
	public String getAssignUserEmail() {
		return assignUserEmail;
	}
	
	public void setAssignUserEmail(String assignUserEmail) {
		this.assignUserEmail = assignUserEmail;
	}
	
	public String getUserUpdatedBy() {
		return userUpdatedBy;
	}
	
	public void setUserUpdatedBy(String userUpdatedBy) {
		this.userUpdatedBy = userUpdatedBy;
	}
	
	public Date getUserUpdatedAt() {
		return userUpdatedAt;
	}
	
	public void setUserUpdatedAt(Date userUpdatedAt) {
		this.userUpdatedAt = userUpdatedAt;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<ObxTaskIdEndTimeListDto> getObxTaskIdEndTimeList() {
		return obxTaskIdEndTimeList;
	}
	
	public void setObxTaskIdEndTimeList(List<ObxTaskIdEndTimeListDto> obxTaskIdEndTimeList) {
		this.obxTaskIdEndTimeList = obxTaskIdEndTimeList;
	}
	
	

	public String getDispatchTaskEmail() {
		return dispatchTaskEmail;
	}

	public void setDispatchTaskEmail(String dispatchTaskEmail) {
		this.dispatchTaskEmail = dispatchTaskEmail;
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

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	@Override
	public String toString() {
		return "ObxTaskUpdateDto [taskId=" + taskId + ", processId=" + processId + ", locationCode=" + locationCode
				+ ", assignUserEmail=" + assignUserEmail + ", userUpdatedBy=" + userUpdatedBy + ", userUpdatedAt="
				+ userUpdatedAt + ", status=" + status + ", dispatchTaskEmail=" + dispatchTaskEmail + ", userGroup="
				+ userGroup + ", obxTaskIdEndTimeList=" + obxTaskIdEndTimeList + "]";
	}

}
