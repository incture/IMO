/**
 * 
 */
package com.murphy.taskmgmt.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Rashmendra.Sai
 *
 */
@Entity
@Table(name = "ATS_TASK_ASSIGNMENT")
public class ATSTaskAssginmentDo implements BaseDo {

	@Id
	@Column(name="TASK_ID",length=80)
	private String taskID;
	
	@Column(name="TASK_OWNER_EMAIL",length=60)
	private String emailID;
	
	@Column(name = "TASK_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTimeOfTask;
	
	@Column(name = "TASK_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTimeOfTask;
	
	@Column(name="SEQUENCE_NUMBER")
	private int sequence;
	
	
	
	@Column(name="LOC_CODE",length=80)
	private String locCode;
	
	@Column(name="LOCATION_TEXT",length=80)
	private String locText;
	
	@Column(name="TIER",length=80)
	private String wellTier;
	
	@Column(name="RISKBBL")
	private BigDecimal risk;
	
	@Column(name="TASK_STOP_TIME")
	private int stopTimeForTask;
	
	@Column(name="OWNER_DESIGNATION",length=60)
	private String userDesignation;
	
	@Column(name="DRIVE_TIME")
	private BigDecimal roadDriveTime;
	
	@Column(name="HAS_TASK_ASSIGNED",length=10)
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

	public Date getStartTimeOfTask() {
		return startTimeOfTask;
	}

	public void setStartTimeOfTask(Date startTimeOfTask) {
		this.startTimeOfTask = startTimeOfTask;
	}

	public Date getEndTimeOfTask() {
		return endTimeOfTask;
	}

	public void setEndTimeOfTask(Date endTimeOfTask) {
		this.endTimeOfTask = endTimeOfTask;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	public String getLocText() {
		return locText;
	}

	public void setLocText(String locText) {
		this.locText = locText;
	}

	public String getWellTier() {
		return wellTier;
	}

	public void setWellTier(String wellTier) {
		this.wellTier = wellTier;
	}

	public BigDecimal getRisk() {
		return risk;
	}

	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
