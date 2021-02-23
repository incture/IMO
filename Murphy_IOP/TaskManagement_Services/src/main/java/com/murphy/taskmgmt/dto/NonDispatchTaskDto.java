package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class NonDispatchTaskDto extends BaseDto {

	private String taskId;	
	private String description;
	private String location;
	private String status;
	private Date createdAt;
	private String createdBy;
	private String createdAtDisplay;
	private Date completedAt;
	private String completedBy;
	private String locType;
	private String group;

	public String getLocType() {
		return locType;
	}



	public void setLocType(String locType) {
		this.locType = locType;
	}

	public Date getCompletedAt() {
		return completedAt;
	}



	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}



	public String getCompletedBy() {
		return completedBy;
	}



	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Date getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	@Override
	public String toString() {
		return "NonDispatchTaskDto [taskId=" + taskId + ", description=" + description + ", location=" + location
				+ ", status=" + status + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", createdAtDisplay="
				+ createdAtDisplay + ", completedAt=" + completedAt + ", completedBy=" + completedBy + ", locType="
				+ locType + ", group=" + group + "]";
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



	public String getCreatedAtDisplay() {
		return createdAtDisplay;
	}



	public void setCreatedAtDisplay(String createdAtDisplay) {
		this.createdAtDisplay = createdAtDisplay;
	}



	public String getGroup() {
		return group;
	}



	public void setGroup(String group) {
		this.group = group;
	}



}
