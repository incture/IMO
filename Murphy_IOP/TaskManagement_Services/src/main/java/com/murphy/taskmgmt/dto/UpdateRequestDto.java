package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class UpdateRequestDto extends BaseDto{

	private String taskId;
	private String ndTaskId;
	private String status;
	private String userId;
	private String userDisplay;
	private String processId;
	private String userUpdatedAt;
	private int currentPosition;
	private int newPosition;
	private List<RootCauseInstDto> rootCauseList;
//	private ResolveIssueDto resolveIssueDto ;
	private String userType;
//	private String endTime;
//	private String startTime;
	private Date endDate;
	private Date startDate;
	private int timeZoneOffSet ;
	private String createdBy;
	private String createdByDisplay;
	private String dateTime;
	//CHG0037344-Inquiry to a field seat
	private String isGroupTask;

	public String getIsGroupTask() {
		return isGroupTask;
	}

	public void setIsGroupTask(String isGroupTask) {
		this.isGroupTask = isGroupTask;
	}
	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedByDisplay() {
		return createdByDisplay;
	}

	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

//	public String getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(String endTime) {
//		this.endTime = endTime;
//	}
//
//	public String getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(String startTime) {
//		this.startTime = startTime;
//	}

//	public ResolveIssueDto getResolveIssueDto() {
//		return resolveIssueDto;
//	}
//
//	public void setResolveIssueDto(ResolveIssueDto resolveIssueDto) {
//		this.resolveIssueDto = resolveIssueDto;
//	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public List<RootCauseInstDto> getRootCauseList() {
		return rootCauseList;
	}

	public void setRootCauseList(List<RootCauseInstDto> rootCauseList) {
		this.rootCauseList = rootCauseList;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}

	public String getUserUpdatedAt() {
		return userUpdatedAt;
	}

	public void setUserUpdatedAt(String userUpdatedAt) {
		this.userUpdatedAt = userUpdatedAt;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UpdateRequestDto [taskId=" + taskId + ", ndTaskId=" + ndTaskId + ", status=" + status + ", userId="
				+ userId + ", userDisplay=" + userDisplay + ", processId=" + processId + ", userUpdatedAt="
				+ userUpdatedAt + ", currentPosition=" + currentPosition + ", newPosition=" + newPosition
				+ ", rootCauseList=" + rootCauseList + ", userType=" + userType + ", endDate=" + endDate
				+ ", startDate=" + startDate + ", timeZoneOffSet=" + timeZoneOffSet + ", createdBy=" + createdBy
				+ ", createdByDisplay=" + createdByDisplay + ", dateTime=" + dateTime + ", isGroupTask=" + isGroupTask
				+ "]";
	}
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {

	}
	public String getNdTaskId() {
		return ndTaskId;
	}
	public void setNdTaskId(String ndTaskId) {
		this.ndTaskId = ndTaskId;
	}
	public String getUserDisplay() {
		return userDisplay;
	}
	public void setUserDisplay(String userDisplay) {
		this.userDisplay = userDisplay;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getTimeZoneOffSet() {
		return timeZoneOffSet;
	}

	public void setTimeZoneOffSet(int timeZoneOffSet) {
		this.timeZoneOffSet = timeZoneOffSet;
	}





}
