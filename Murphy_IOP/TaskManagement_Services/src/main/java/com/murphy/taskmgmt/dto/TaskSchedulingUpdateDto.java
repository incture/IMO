package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

public class TaskSchedulingUpdateDto {

	private List<TaskSchedulingDto> taskSchedulingDtoListForUser;
	private Date newStartDate;
	private double customOffset;
	private String taskId;
	private Boolean isTaskShift;
	private String taskOwner;

	private Date taskNextOldLocationEndDate;
	private Double taskNextOldLocationDriveTime;
	private String taskNextOldLocationTaskId;
	private double taskNextOldLocationTotalTime;
	private Date taskNextNewLocationEndDate;
	private Double taskNextNewLocationDriveTime;
	private String taskNextNewLocationTaskId;
	private double taskNextNewLocationTotalTime;
	private Date shiftedTaskEndDate;
	private Double shiftedTaskDriveTime;
	private String shiftedTaskTaskId;
	private double shiftedTaskTotalTime;
	private Date shiftedTaskStartDate;
	private double shiftedTaskCustomTime;
	private String newStatus;
	public List<TaskSchedulingDto> getTaskSchedulingDtoListForUser() {
		return taskSchedulingDtoListForUser;
	}
	public void setTaskSchedulingDtoListForUser(List<TaskSchedulingDto> taskSchedulingDtoListForUser) {
		this.taskSchedulingDtoListForUser = taskSchedulingDtoListForUser;
	}
	public Date getNewStartDate() {
		return newStartDate;
	}
	public void setNewStartDate(Date newStartDate) {
		this.newStartDate = newStartDate;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Boolean getIsTaskShift() {
		return isTaskShift;
	}
	public void setIsTaskShift(Boolean isTaskShift) {
		this.isTaskShift = isTaskShift;
	}
	public String getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}
	public Date getTaskNextOldLocationEndDate() {
		return taskNextOldLocationEndDate;
	}
	public void setTaskNextOldLocationEndDate(Date taskNextOldLocationEndDate) {
		this.taskNextOldLocationEndDate = taskNextOldLocationEndDate;
	}
	public Double getTaskNextOldLocationDriveTime() {
		return taskNextOldLocationDriveTime;
	}
	public void setTaskNextOldLocationDriveTime(Double taskNextOldLocationDriveTime) {
		this.taskNextOldLocationDriveTime = taskNextOldLocationDriveTime;
	}
	public String getTaskNextOldLocationTaskId() {
		return taskNextOldLocationTaskId;
	}
	public void setTaskNextOldLocationTaskId(String taskNextOldLocationTaskId) {
		this.taskNextOldLocationTaskId = taskNextOldLocationTaskId;
	}
	public double getTaskNextOldLocationTotalTime() {
		return taskNextOldLocationTotalTime;
	}
	public void setTaskNextOldLocationTotalTime(double taskNextOldLocationTotalTime) {
		this.taskNextOldLocationTotalTime = taskNextOldLocationTotalTime;
	}
	public Date getTaskNextNewLocationEndDate() {
		return taskNextNewLocationEndDate;
	}
	public void setTaskNextNewLocationEndDate(Date taskNextNewLocationEndDate) {
		this.taskNextNewLocationEndDate = taskNextNewLocationEndDate;
	}
	public Double getTaskNextNewLocationDriveTime() {
		return taskNextNewLocationDriveTime;
	}
	public void setTaskNextNewLocationDriveTime(Double taskNextNewLocationDriveTime) {
		this.taskNextNewLocationDriveTime = taskNextNewLocationDriveTime;
	}
	public String getTaskNextNewLocationTaskId() {
		return taskNextNewLocationTaskId;
	}
	public void setTaskNextNewLocationTaskId(String taskNextNewLocationTaskId) {
		this.taskNextNewLocationTaskId = taskNextNewLocationTaskId;
	}
	public double getTaskNextNewLocationTotalTime() {
		return taskNextNewLocationTotalTime;
	}
	public void setTaskNextNewLocationTotalTime(double taskNextNewLocationTotalTime) {
		this.taskNextNewLocationTotalTime = taskNextNewLocationTotalTime;
	}
	public Date getShiftedTaskEndDate() {
		return shiftedTaskEndDate;
	}
	public void setShiftedTaskEndDate(Date shiftedTaskEndDate) {
		this.shiftedTaskEndDate = shiftedTaskEndDate;
	}
	public Double getShiftedTaskDriveTime() {
		return shiftedTaskDriveTime;
	}
	public double getShiftedTaskCustomTime() {
		return shiftedTaskCustomTime;
	}
	public void setShiftedTaskCustomTime(double shiftedTaskCustomTime) {
		this.shiftedTaskCustomTime = shiftedTaskCustomTime;
	}
	public void setShiftedTaskDriveTime(Double shiftedTaskDriveTime) {
		this.shiftedTaskDriveTime = shiftedTaskDriveTime;
	}
	public String getShiftedTaskTaskId() {
		return shiftedTaskTaskId;
	}
	public void setShiftedTaskTaskId(String shiftedTaskTaskId) {
		this.shiftedTaskTaskId = shiftedTaskTaskId;
	}
	public double getShiftedTaskTotalTime() {
		return shiftedTaskTotalTime;
	}
	public void setShiftedTaskTotalTime(double shiftedTaskTotalTime) {
		this.shiftedTaskTotalTime = shiftedTaskTotalTime;
	}
	public Date getShiftedTaskStartDate() {
		return shiftedTaskStartDate;
	}
	public void setShiftedTaskStartDate(Date shiftedTaskStartDate) {
		this.shiftedTaskStartDate = shiftedTaskStartDate;
	}
	public double getCustomOffset() {
		return customOffset;
	}
	public void setCustomOffset(double customOffset) {
		this.customOffset = customOffset;
	}
	@Override
	public String toString() {
		return "TaskSchedulingUpdateDto [newStartDate=" + newStartDate + ", customOffset=" + customOffset + ", taskId=" + taskId
				+ ", isTaskShift=" + isTaskShift + ", taskOwner=" + taskOwner + ", taskNextOldLocationEndDate="
				+ taskNextOldLocationEndDate + ", taskNextOldLocationDriveTime=" + taskNextOldLocationDriveTime
				+ ", taskNextOldLocationTaskId=" + taskNextOldLocationTaskId + ", taskNextOldLocationTotalTime="
				+ taskNextOldLocationTotalTime + ", taskNextNewLocationEndDate=" + taskNextNewLocationEndDate
				+ ", taskNextNewLocationDriveTime=" + taskNextNewLocationDriveTime + ", taskNextNewLocationTaskId="
				+ taskNextNewLocationTaskId + ", taskNextNewLocationTotalTime=" + taskNextNewLocationTotalTime
				+ ", shiftedTaskEndDate=" + shiftedTaskEndDate + ", shiftedTaskDriveTime=" + shiftedTaskDriveTime
				+ ", shiftedTaskTaskId=" + shiftedTaskTaskId + ", shiftedTaskTotalTime=" + shiftedTaskTotalTime
				+ ", shiftedTaskStartDate=" + shiftedTaskStartDate + ", shiftedTaskCustomTime=" + shiftedTaskCustomTime
				+ ", newStatus=" + newStatus + "]";
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	

	

}
