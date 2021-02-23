package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class TaskSubmittedDto extends BaseDto {

	private String createdAtInString;
	private String Team;
	private String user;
	private String issueCategory;
	private String description;
	private String status;
	private String completedAtInString;


	public String getCreatedAtInString() {
		return createdAtInString;
	}

	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}

	public String getTeam() {
		return Team;
	}

	public void setTeam(String team) {
		Team = team;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIssueCategory() {
		return issueCategory;
	}

	public void setIssueCategory(String issueCategory) {
		this.issueCategory = issueCategory;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "TaskSubmittedDto [createdAtInString=" + createdAtInString + ", Team=" + Team + ", user=" + user
				+ ", issueCategory=" + issueCategory + ", description=" + description + ", status=" + status + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompletedAtInString() {
		return completedAtInString;
	}

	public void setCompletedAtInString(String completedAtInString) {
		this.completedAtInString = completedAtInString;
	}
	
}
