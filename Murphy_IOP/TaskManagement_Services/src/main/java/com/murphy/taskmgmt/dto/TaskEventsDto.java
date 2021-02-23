package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class TaskEventsDto extends BaseDto {
	private String createdByDisplay;
	private String createdBy;
	private String requestId;
	private String taskId;
	private String processId;
	private String description;
	private String subject;
	private String name;
	private String status;
	private String currentProcessor;
	private String priority;
	private Date createdAt;
	private Date completedAt;
	private Date completionDeadLine;
	private List<TaskOwnersDto> owners;
	private String createdAtInString;
	private String completedAtInString;
	private String completionDeadLineInString;
	private String currentProcessorDisplayName;
	private String processName;
	private String statusFlag;
	private String taskMode;
	private String taskType;
	private Date forwardedAt;
	private String forwardedBy;
	private String forwardedAtInString;
	private String origin;
	private String detailUrl;
	private String prevTask;
	private String group;
	private Date updatedAt;
	private String updatedBy;
	private Date userUpdatedAt;
	private String locationCode;
	private String subClassification;
	private boolean hasDispatchTask;
	
	/*For Inquiry task*/
	private String parentOrigin;
	private String locationText;
	private String locationType;
	private String taskRefNum;
	private String muwiID;
	private String tier;
	
	/*For Sysytem Generated OBX tak */
	private Double driveTime;
	
	private String start_time;
	
	//Start-CHG0037344-Inquiry to a field seat.
	private boolean isEditable;
	private String isGroupTask;
	//End-CHG0037344-Inquiry to a field seat.
	
	//Flag added for future task notification
	private boolean notificationFlag;

	public String getTier() {
		return tier;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getIsGroupTask() {
		return isGroupTask;
	}

	public void setIsGroupTask(String isGroupTask) {
		this.isGroupTask = isGroupTask;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getTaskRefNum() {
		return taskRefNum;
	}

	public void setTaskRefNum(String taskRefNum) {
		this.taskRefNum = taskRefNum;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	

	public Date getUserUpdatedAt() {
		return userUpdatedAt;
	}

	public void setUserUpdatedAt(Date userUpdatedAt) {
		this.userUpdatedAt = userUpdatedAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getPrevTask() {
		return prevTask;
	}

	public void setPrevTask(String prevTask) {
		this.prevTask = prevTask;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrentProcessor() {
		return currentProcessor;
	}

	public void setCurrentProcessor(String currentProcessor) {
		this.currentProcessor = currentProcessor;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public Date getCompletionDeadLine() {
		return completionDeadLine;
	}

	public void setCompletionDeadLine(Date completionDeadLine) {
		this.completionDeadLine = completionDeadLine;
	}

	public List<TaskOwnersDto> getOwners() {
		return owners;
	}

	public void setOwners(List<TaskOwnersDto> owners) {
		this.owners = owners;
	}

	public String getCreatedAtInString() {
		return createdAtInString;
	}

	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}

	public String getCompletedAtInString() {
		return completedAtInString;
	}

	public void setCompletedAtInString(String completedAtInString) {
		this.completedAtInString = completedAtInString;
	}

	public String getCompletionDeadLineInString() {
		return completionDeadLineInString;
	}

	public void setCompletionDeadLineInString(String completionDeadLineInString) {
		this.completionDeadLineInString = completionDeadLineInString;
	}

	public String getCurrentProcessorDisplayName() {
		return currentProcessorDisplayName;
	}

	public void setCurrentProcessorDisplayName(String currentProcessorDisplayName) {
		this.currentProcessorDisplayName = currentProcessorDisplayName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getTaskMode() {
		return taskMode;
	}

	public void setTaskMode(String taskMode) {
		this.taskMode = taskMode;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Date getForwardedAt() {
		return forwardedAt;
	}

	public void setForwardedAt(Date forwardedAt) {
		this.forwardedAt = forwardedAt;
	}

	public String getForwardedBy() {
		return forwardedBy;
	}

	public void setForwardedBy(String forwardedBy) {
		this.forwardedBy = forwardedBy;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getForwardedAtInString() {
		return forwardedAtInString;
	}

	public void setForwardedAtInString(String forwardedAtInString) {
		this.forwardedAtInString = forwardedAtInString;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCreatedByDisplay() {
		return createdByDisplay;
	}

	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}
	
	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
	public String getParentOrigin() {
		return parentOrigin;
	}

	public void setParentOrigin(String parentOrigin) {
		this.parentOrigin = parentOrigin;
	}

	public boolean isHasDispatchTask() {
		return hasDispatchTask;
	}

	public void setHasDispatchTask(boolean hasDispatchTask) {
		this.hasDispatchTask = hasDispatchTask;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public Double getDriveTime() {
		return driveTime;
	}

	public void setDriveTime(Double driveTime) {
		this.driveTime = driveTime;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	public String getMuwiID() {
		return muwiID;
	}

	public void setMuwiID(String muwiID) {
		this.muwiID = muwiID;
	}

	@Override
	public String toString() {
		return "TaskEventsDto [createdByDisplay=" + createdByDisplay + ", createdBy=" + createdBy + ", requestId="
				+ requestId + ", taskId=" + taskId + ", processId=" + processId + ", description=" + description
				+ ", subject=" + subject + ", name=" + name + ", status=" + status + ", currentProcessor="
				+ currentProcessor + ", priority=" + priority + ", createdAt=" + createdAt + ", completedAt="
				+ completedAt + ", completionDeadLine=" + completionDeadLine + ", owners=" + owners
				+ ", createdAtInString=" + createdAtInString + ", completedAtInString=" + completedAtInString
				+ ", completionDeadLineInString=" + completionDeadLineInString + ", currentProcessorDisplayName="
				+ currentProcessorDisplayName + ", processName=" + processName + ", statusFlag=" + statusFlag
				+ ", taskMode=" + taskMode + ", taskType=" + taskType + ", forwardedAt=" + forwardedAt
				+ ", forwardedBy=" + forwardedBy + ", forwardedAtInString=" + forwardedAtInString + ", origin=" + origin
				+ ", detailUrl=" + detailUrl + ", prevTask=" + prevTask + ", group=" + group + ", updatedAt="
				+ updatedAt + ", updatedBy=" + updatedBy + ", userUpdatedAt=" + userUpdatedAt + ", locationCode="
				+ locationCode + ", subClassification=" + subClassification + ", hasDispatchTask=" + hasDispatchTask
				+ ", parentOrigin=" + parentOrigin + ", locationText=" + locationText + ", locationType=" + locationType
				+ ", taskRefNum=" + taskRefNum + ", muwiID=" + muwiID + ", tier=" + tier + ", driveTime=" + driveTime
				+ ", start_time=" + start_time + ", isEditable=" + isEditable + ", isGroupTask=" + isGroupTask
				+ ", notificationFlag=" + notificationFlag + "]";
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public boolean isNotificationFlag() {
		return notificationFlag;
	}

	public void setNotificationFlag(boolean notificationFlag) {
		this.notificationFlag = notificationFlag;
	}

}
