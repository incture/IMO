package com.murphy.taskmgmt.dto;

import java.util.List;

public class UserDetailsDto {
	
	private String userId;
	private String userName;
	private String emailId;
	private List<String> groups;
	private List<String> roles;
	private boolean isExists;
	
	public boolean getIsExists() {
		return isExists;
	}
	public void setIsExists(boolean isExists) {
		this.isExists = isExists;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "UserDetailsDto [userId=" + userId + ", userName=" + userName + ", emailId=" + emailId + ", groups="
				+ groups + ", roles=" + roles + ", isExists=" + isExists + "]";
	}
	
	
	
	
	
	
	
	
	
}
