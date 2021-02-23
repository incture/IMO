package com.murphy.taskmgmt.dto;

public class NearbyTaskDto {
	// Not for display, used for reassignment
	private String taskId;
	private String processId;
	// Field to be displayed
	private String taskDescription;
	private String locationText;
	private String locationCode;
	private float estimatedCompletionTime;
	private float estimatedTravelTime;
	private float estimatedTotalDuration;
	private String assignee; // TaskOwner
	private String assigneeName;
	// For roundDistance calculation
	private Double toLatitude;
	private Double toLongitude;
	private String subClassification;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public float getEstimatedCompletionTime() {
		return estimatedCompletionTime;
	}

	public void setEstimatedCompletionTime(float estimatedCompletionTime) {
		this.estimatedCompletionTime = estimatedCompletionTime;
	}

	public float getEstimatedTravelTime() {
		return estimatedTravelTime;
	}

	public void setEstimatedTravelTime(float estimatedTravelTime) {
		this.estimatedTravelTime = estimatedTravelTime;
	}

	public float getEstimatedTotalDuration() {
		return estimatedTotalDuration;
	}

	public void setEstimatedTotalDuration(float estimatedTotalDuration) {
		this.estimatedTotalDuration = estimatedTotalDuration;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Double getToLatitude() {
		return toLatitude;
	}

	public void setToLatitude(Double toLatitude) {
		this.toLatitude = toLatitude;
	}

	public Double getToLongitude() {
		return toLongitude;
	}

	public void setToLongitude(Double toLongitude) {
		this.toLongitude = toLongitude;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
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

	
	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	@Override
	public String toString() {
		return "NearbyTaskDto [taskId=" + taskId + ", processId=" + processId + ", taskDescription=" + taskDescription
				+ ", locationText=" + locationText + ", locationCode=" + locationCode + ", estimatedCompletionTime="
				+ estimatedCompletionTime + ", estimatedTravelTime=" + estimatedTravelTime + ", estimatedTotalDuration="
				+ estimatedTotalDuration + ", assignee=" + assignee + ", assigneeName=" + assigneeName + ", toLatitude="
				+ toLatitude + ", toLongitude=" + toLongitude + ", subClassification=" + subClassification + "]";
	}


}