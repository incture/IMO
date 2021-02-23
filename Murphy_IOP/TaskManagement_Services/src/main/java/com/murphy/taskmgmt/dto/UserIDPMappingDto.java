package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class UserIDPMappingDto extends BaseDto {

	private String serialId;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userRole;
	private String userLoginName;
	private String taskAssignable;
	private int taskCount;
	private String pId;
	private String blackLineId;
	private String sapId;

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getBlackLineId() {
		return blackLineId;
	}

	public void setBlackLineId(String blackLineId) {
		this.blackLineId = blackLineId;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getTaskAssignable() {
		return taskAssignable;
	}

	public void setTaskAssignable(String taskAssignable) {
		this.taskAssignable = taskAssignable;
	}

	

	@Override
	public String toString() {
		return "UserIDPMappingDto [serialId=" + serialId + ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + ", userEmail=" + userEmail + ", userRole=" + userRole + ", userLoginName="
				+ userLoginName + ", taskAssignable=" + taskAssignable + ", taskCount=" + taskCount + ", pId=" + pId
				+ ", blackLineId=" + blackLineId + ", sapId=" + sapId + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
	
	
}
