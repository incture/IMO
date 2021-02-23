package com.murphy.taskmgmt.dto;

import java.util.Date;

public class TaskSchedulingDto {

	private String taskId;
	private String createdAtDisplay;
	private int priority;
	private String location;
	private String classification;
	private String subClassification;
	private float estResolveTime;
	private float totalEstTime;
	private float travelTime;
	private Date startTime;
	private String startTimeInString;
	private Date endTime;
	private String endTimeInString;
	private String status;
	private String tier;
	private float customOffset;
	private Date expectedStartTime;
	private Date expectedEndTime;
	private String expectedStartTimeInString;
	private String expectedEndTimeInString;
	private String processId;
	// private String locationCode;

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	@Override
	public String toString() {
		return "TaskSchedulingDto [taskId=" + taskId + ", createdAtDisplay=" + createdAtDisplay + ", priority="
				+ priority + ", location=" + location + ", classification=" + classification + ", subClassification="
				+ subClassification + ", estResolveTime=" + estResolveTime + ", totalEstTime=" + totalEstTime
				+ ", travelTime=" + travelTime + ", startTime=" + startTime + ", startTimeInString=" + startTimeInString
				+ ", endTime=" + endTime + ", endTimeInString=" + endTimeInString + ", status=" + status + ", tier="
				+ tier + ", customOffset=" + customOffset + ", expectedStartTime=" + expectedStartTime
				+ ", expectedEndTime=" + expectedEndTime + ", expectedStartTimeInString=" + expectedStartTimeInString
				+ ", expectedEndTimeInString=" + expectedEndTimeInString + ", processId=" + processId + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAtDisplay() {
		return createdAtDisplay;
	}

	public void setCreatedAtDisplay(String createdAtDisplay) {
		this.createdAtDisplay = createdAtDisplay;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public float getEstResolveTime() {
		return estResolveTime;
	}

	public void setEstResolveTime(float estResolveTime) {
		this.estResolveTime = estResolveTime;
	}

	public float getTotalEstTime() {
		return totalEstTime;
	}

	public void setTotalEstTime(float totalEstTime) {
		this.totalEstTime = totalEstTime;
	}

	public float getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(float travelTime) {
		this.travelTime = travelTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStartTimeInString() {
		return startTimeInString;
	}

	public void setStartTimeInString(String startTimeInString) {
		this.startTimeInString = startTimeInString;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getEndTimeInString() {
		return endTimeInString;
	}

	public void setEndTimeInString(String endTimeInString) {
		this.endTimeInString = endTimeInString;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public float getCustomOffset() {
		return customOffset;
	}

	public void setCustomOffset(float customOffset) {
		this.customOffset = customOffset;
	}

	public Date getExpectedStartTime() {
		return expectedStartTime;
	}

	public void setExpectedStartTime(Date expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	public Date getExpectedEndTime() {
		return expectedEndTime;
	}

	public void setExpectedEndTime(Date expectedEndTime) {
		this.expectedEndTime = expectedEndTime;
	}

	public String getExpectedStartTimeInString() {
		return expectedStartTimeInString;
	}

	public void setExpectedStartTimeInString(String expectedStartTimeInString) {
		this.expectedStartTimeInString = expectedStartTimeInString;
	}

	public String getExpectedEndTimeInString() {
		return expectedEndTimeInString;
	}

	public void setExpectedEndTimeInString(String expectedEndTimeInString) {
		this.expectedEndTimeInString = expectedEndTimeInString;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
	

}
