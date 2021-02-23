package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: ProcessEventsDo
 *
 */
@Entity
@Table(name = "TM_TASK_OWNER")
public class TaskOwnersDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public TaskOwnersDo() {
		super();
	}

	private static final long serialVersionUID = 8966817427208717661L;

	@EmbeddedId
	private TaskOwnersDoPK taskOwnersDoPK;

//	@Column(name = "IS_PROCESSED")
//	private Boolean isProcessed;
//	
//	@Column(name = "IS_SUBSTITUTED")
//	private Boolean isSubstituted;
	
	@Column(name = "TASK_OWNER_DISP", length = 100)
	private String taskOwnerDisplayName;
	
	@Column(name = "TASK_OWNER_EMAIL", length = 60)
	private String ownerEmail;
	
	@Column(name = "PRIORITY")
	private int priority;
	
	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name = "EST_RESOLVE_TIME")
	private double estResolveTime;
	
	@Column(name = "EST_DRIVE_TIME")
	private double estDriveTime;

	@Column(name = "CUSTOM_TIME")
	private double customTime;
	
	@Column(name = "TIER", length = 60)
	private String tier;
	
	@Column(name = "P_ID", length =10)
	private String pId;
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
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

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public TaskOwnersDoPK getTaskOwnersDoPK() {
		return taskOwnersDoPK;
	}

	public void setTaskOwnersDoPK(TaskOwnersDoPK taskOwnersDoPK) {
		this.taskOwnersDoPK = taskOwnersDoPK;
	}

//	public Boolean getIsProcessed() {
//		return isProcessed;
//	}
//
//	public void setIsProcessed(Boolean isProcessed) {
//		this.isProcessed = isProcessed;
//	}
//	
//	public Boolean getIsSubstituted() {
//		return isSubstituted;
//	}
//
//	public void setIsSubstituted(Boolean isSubstituted) {
//		this.isSubstituted = isSubstituted;
//	}


	@Override
	public String toString() {
		return "TaskOwnersDo [taskOwnersDoPK=" + taskOwnersDoPK + ", taskOwnerDisplayName=" + taskOwnerDisplayName
				+ ", ownerEmail=" + ownerEmail + ", priority=" + priority + ", startTime=" + startTime + ", endTime="
				+ endTime + ", estResolveTime=" + estResolveTime + ", estDriveTime=" + estDriveTime + ", customTime="
				+ customTime + ", tier=" + tier + ", pId = "+pId+"]";
	}

	public String getTaskOwnerDisplayName() {
		return taskOwnerDisplayName;
	}

	public void setTaskOwnerDisplayName(String taskOwnerDisplayName) {
		this.taskOwnerDisplayName = taskOwnerDisplayName;
	}

	@Override
	public Object getPrimaryKey() {
		return taskOwnersDoPK;
	}

}
