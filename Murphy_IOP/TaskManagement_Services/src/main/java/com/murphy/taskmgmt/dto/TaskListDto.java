package com.murphy.taskmgmt.dto;

import java.util.Date;

public class TaskListDto {

	private String taskId;
	private String processId;
	private String description;
	private String taskOwner;
	private String Status;
	private String latestComment;
	private String commentedAt;
	private String commentedByDisplay;
	private String createdBy;
	private long requestId;
	private Date createdAt;
	private String createdAtInString;
	private String taskTimeDifference;
	private String location;
	private String origin;
	private String parentOrigin;
	private String locationCode;
	private String muwiId;
	private String createdByEmailId;
	private String creatorGroupId;
	private boolean hasDispatch;
	private String rootCause;	
	private Double piggingTimeMinuts;
	private Date piggingDateTime;
	private String classification; // Created for location history
	// Created for Incident INC0079467
	private String createdOnGroup; // user_group

	// Created for location history
	private String taskType;
	private String turnAroundTime;
	private String turnAroundVariance;
	private String prev_task;
	private Date updated_at;
	private String updated_by;
	private Date user_updated_at;
	private String extra_role;

	// Adding for Task Filters
	private String taskClassification;
	private String subClassification;
	private String issueClassification;
	private String tier;
	private String taskRefNum;
	private String start_time;
	
	// Adding rootCauseDescription
	private String rootCauseDescription;
	
	private String taskDescription;
	
	//distance of next task from current location of operator
	private Double distance;
	
	// Added to check bypass/EI existence on location
	private boolean hasBypass;
	private boolean hasEnergyIsolation;
	private String loc_type;
	
	public String getLoc_type() {
		return loc_type;
	}

	public void setLoc_type(String loc_type) {
		this.loc_type = loc_type;
	}

	public boolean isHasBypass() {
		return hasBypass;
	}

	public void setHasBypass(boolean hasBypass) {
		this.hasBypass = hasBypass;
	}

	public boolean isHasEnergyIsolation() {
		return hasEnergyIsolation;
	}

	public void setHasEnergyIsolation(boolean hasEnergyIsolation) {
		this.hasEnergyIsolation = hasEnergyIsolation;
	}

	
	
	public String getRootCauseDescription() {
		return rootCauseDescription;
	}

	public void setRootCauseDescription(String rootCauseDescription) {
		this.rootCauseDescription = rootCauseDescription;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getTaskTimeDifference() {
		return taskTimeDifference;
	}

	public void setTaskTimeDifference(String taskTimeDifference) {
		this.taskTimeDifference = taskTimeDifference;
	}

	public String getTaskRefNum() {
		return taskRefNum;
	}

	public void setTaskRefNum(String taskRefNum) {
		this.taskRefNum = taskRefNum;
	}

	public String getTaskClassification() {
		return taskClassification;
	}

	public void setTaskClassification(String taskClassification) {
		this.taskClassification = taskClassification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public String getIssueClassification() {
		return issueClassification;
	}

	public void setIssueClassification(String issueClassification) {
		this.issueClassification = issueClassification;
	}

	public Double getPiggingTimeMinuts() {
		return piggingTimeMinuts;
	}

	public void setPiggingTimeMinuts(Double piggingTimeMinuts) {
		this.piggingTimeMinuts = piggingTimeMinuts;
	}

	public Date getPiggingDateTime() {
		return piggingDateTime;
	}

	public void setPiggingDateTime(Date piggingDateTime) {
		this.piggingDateTime = piggingDateTime;
	}

	public String getParentOrigin() {
		return parentOrigin;
	}

	public void setParentOrigin(String parentOrigin) {
		this.parentOrigin = parentOrigin;
	}

	public String getCreatedByEmailId() {
		return createdByEmailId;
	}

	public void setCreatedByEmailId(String createdByEmailId) {
		this.createdByEmailId = createdByEmailId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(String latestComment) {
		this.latestComment = latestComment;
	}

	public String getCommentedAt() {
		return commentedAt;
	}

	public void setCommentedAt(String commentedAt) {
		this.commentedAt = commentedAt;
	}

	public String getCommentedByDisplay() {
		return commentedByDisplay;
	}

	public void setCommentedByDisplay(String commentedByDisplay) {
		this.commentedByDisplay = commentedByDisplay;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getCreatedAtInString() {
		return createdAtInString;
	}

	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getMuwiId() {
		return muwiId;
	}

	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	

	public boolean isHasDispatch() {
		return hasDispatch;
	}

	public void setHasDispatch(boolean hasDispatch) {
		this.hasDispatch = hasDispatch;
	}

	public String getCreatorGroupId() {
		return creatorGroupId;
	}

	public void setCreatorGroupId(String creatorGroupId) {
		this.creatorGroupId = creatorGroupId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getCreatedOnGroup() {
		return createdOnGroup;
	}

	public void setCreatedOnGroup(String createdOnGroup) {
		this.createdOnGroup = createdOnGroup;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTurnAroundVariance() {
		return turnAroundVariance;
	}

	public void setTurnAroundVariance(String turnAroundVariance) {
		this.turnAroundVariance = turnAroundVariance;
	}

	public String getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(String turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public String getPrev_task() {
		return prev_task;
	}

	public void setPrev_task(String prev_task) {
		this.prev_task = prev_task;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUser_updated_at() {
		return user_updated_at;
	}

	public void setUser_updated_at(Date user_updated_at) {
		this.user_updated_at = user_updated_at;
	}

	public String getExtra_role() {
		return extra_role;
	}

	public void setExtra_role(String extra_role) {
		this.extra_role = extra_role;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Override
	public String toString() {
		return "TaskListDto [taskId=" + taskId + ", processId=" + processId + ", description=" + description
				+ ", taskOwner=" + taskOwner + ", Status=" + Status + ", latestComment=" + latestComment
				+ ", commentedAt=" + commentedAt + ", commentedByDisplay=" + commentedByDisplay + ", createdBy="
				+ createdBy + ", requestId=" + requestId + ", createdAt=" + createdAt + ", createdAtInString="
				+ createdAtInString + ", taskTimeDifference=" + taskTimeDifference + ", location=" + location
				+ ", origin=" + origin + ", parentOrigin=" + parentOrigin + ", locationCode=" + locationCode
				+ ", muwiId=" + muwiId + ", createdByEmailId=" + createdByEmailId + ", creatorGroupId=" + creatorGroupId
				+ ", hasDispatch=" + hasDispatch + ", rootCause=" + rootCause + ", piggingTimeMinuts="
				+ piggingTimeMinuts + ", piggingDateTime=" + piggingDateTime + ", classification=" + classification
				+ ", createdOnGroup=" + createdOnGroup + ", taskType=" + taskType + ", turnAroundTime=" + turnAroundTime
				+ ", turnAroundVariance=" + turnAroundVariance + ", prev_task=" + prev_task + ", updated_at="
				+ updated_at + ", updated_by=" + updated_by + ", user_updated_at=" + user_updated_at + ", extra_role="
				+ extra_role + ", taskClassification=" + taskClassification + ", subClassification=" + subClassification
				+ ", issueClassification=" + issueClassification + ", tier=" + tier + ", taskRefNum=" + taskRefNum
				+ ", start_time=" + start_time + ", rootCauseDescription=" + rootCauseDescription + ", taskDescription="
				+ taskDescription + ", distance=" + distance + ", hasBypass=" + hasBypass + ", hasEnergyIsolation="
				+ hasEnergyIsolation + ", loc_type=" + loc_type + "]";
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	
	
	

}
