package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class TaskOwnersDto extends BaseDto{

	private String taskId;
	private String taskOwner;
	private String taskOwnerDisplayName;
	private String ownerEmail;
	private int priority;
	private Date startTime;
	private Date endTime;
	private double estResolveTime;
	private double estDriveTime;
	private double customTime;
	private String tier;
	private String pId;
//	private Boolean isProcessed;
//	private Boolean isSubstituted;
	private String gainTime;
	private String taskStatus;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public double getEstResolveTime() {
		return estResolveTime;
	}

	public void setEstResolveTime(double estResolveTime) {
		this.estResolveTime = estResolveTime;
	}

	public double getEstDriveTime() {
		return estDriveTime;
	}

	public void setEstDriveTime(double estDriveTime) {
		this.estDriveTime = estDriveTime;
	}



	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

//	public Boolean getIsProcessed() {
//		return isProcessed;
//	}
//
//	public void setIsProcessed(Boolean isProcessed) {
//		this.isProcessed = isProcessed;
//	}

	public String getTaskOwnerDisplayName() {
		return taskOwnerDisplayName;
	}

	public void setTaskOwnerDisplayName(String taskOwnerDisplayName) {
		this.taskOwnerDisplayName = taskOwnerDisplayName;
	}


//	public Boolean getIsSubstituted() {
//		return isSubstituted;
//	}
//
//	public void setIsSubstituted(Boolean isSubstituted) {
//		this.isSubstituted = isSubstituted;
//	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "TaskOwnersDto [taskId=" + taskId + ", taskOwner=" + taskOwner + ", taskOwnerDisplayName="
				+ taskOwnerDisplayName + ", ownerEmail=" + ownerEmail + ", priority=" + priority + ", startTime="
				+ startTime + ", endTime=" + endTime + ", estResolveTime=" + estResolveTime + ", estDriveTime="
				+ estDriveTime + ", customTime=" + customTime + ", tier=" + tier + ", pId=" + pId + ", gainTime="
				+ gainTime + ", taskStatus=" + taskStatus + "]";
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public double getCustomTime() {
		return customTime;
	}

	public void setCustomTime(double customTime) {
		this.customTime = customTime;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getGainTime() {
		return gainTime;
	}

	public void setGainTime(String gainTime) {
		this.gainTime = gainTime;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	

}
