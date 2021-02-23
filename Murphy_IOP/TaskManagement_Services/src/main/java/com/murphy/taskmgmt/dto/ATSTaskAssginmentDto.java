/**
 * 
 */
package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

/**
 * @author Rashmendra.Sai
 *
 */
public class ATSTaskAssginmentDto extends BaseDto {

	private String taskID;
	private String emailID;
	private Date startTimeOfTask;
	private Date endTimeOfTask;
	private int sequence;
	private String locCode;
	private String locText;
	private String wellTier;
	private BigDecimal risk;
	private int stopTimeForTask;
	private String userDesignation;
	private BigDecimal roadDriveTime;
	
	private String hasAssigned;
	
	
	public String getHasAssigned() {
		return hasAssigned;
	}
	public void setHasAssigned(String hasAssigned) {
		this.hasAssigned = hasAssigned;
	}
	public BigDecimal getRoadDriveTime() {
		return roadDriveTime;
	}
	public void setRoadDriveTime(BigDecimal roadDriveTime) {
		this.roadDriveTime = roadDriveTime;
	}
	public String getUserDesignation() {
		return userDesignation;
	}
	public void setUserDesignation(String userDesignation) {
		this.userDesignation = userDesignation;
	}
	public int getStopTimeForTask() {
		return stopTimeForTask;
	}
	public void setStopTimeForTask(int stopTimeForTask) {
		this.stopTimeForTask = stopTimeForTask;
	}
	
	public BigDecimal getRisk() {
		return risk;
	}
	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}
	public String getWellTier() {
		return wellTier;
	}
	public void setWellTier(String wellTier) {
		this.wellTier = wellTier;
	}
	public String getLocText() {
		return locText;
	}
	public void setLocText(String locText) {
		this.locText = locText;
	}
	public Date getEndTimeOfTask() {
		return endTimeOfTask;
	}
	public void setEndTimeOfTask(Date endTimeOfTask) {
		this.endTimeOfTask = endTimeOfTask;
	}
	public String getLocCode() {
		return locCode;
	}
	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public Date getStartTimeOfTask() {
		return startTimeOfTask;
	}
	public void setStartTimeOfTask(Date startTimeOfTask) {
		this.startTimeOfTask = startTimeOfTask;
	}
	@Override
	public String toString() {
		return "ATSTaskAssginmentDto [taskID=" + taskID + ", emailID=" + emailID + ", startTimeOfTask="
				+ startTimeOfTask + ", endTimeOfTask=" + endTimeOfTask + ", sequence=" + sequence + ", locCode="
				+ locCode + ", locText=" + locText + ", wellTier=" + wellTier + ", risk=" + risk + ", stopTimeForTask="
				+ stopTimeForTask + ", userDesignation=" + userDesignation + ", roadDriveTime=" + roadDriveTime
				+ ", hasAssigned=" + hasAssigned + "]";
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
	
	
}
