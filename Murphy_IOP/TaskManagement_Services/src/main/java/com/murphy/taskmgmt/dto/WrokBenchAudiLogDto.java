package com.murphy.taskmgmt.dto;

import java.util.Date;

public class WrokBenchAudiLogDto {
	
	private String auditLogId;
	private String taskId;
	/*private String attrTempId;
	private String insValue;*/
	private String taskType;
	private String tier;
	private String locationCode;
	private String locationType;
	private String source;
	private String action;
	private String actionBy;
	private String actionByEmail;
	private String reason;
	private String createdAt;
	
	/**
	 * @return the auditLogId
	 */
	public String getAuditLogId() {
		return auditLogId;
	}
	/**
	 * @param auditLogId the auditLogId to set
	 */
	public void setAuditLogId(String auditLogId) {
		this.auditLogId = auditLogId;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the tier
	 */
	public String getTier() {
		return tier;
	}
	/**
	 * @param tier the tier to set
	 */
	public void setTier(String tier) {
		this.tier = tier;
	}
	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}
	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}
	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the actionBy
	 */
	public String getActionBy() {
		return actionBy;
	}
	/**
	 * @param actionBy the actionBy to set
	 */
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	/**
	 * @return the actionByEmail
	 */
	public String getActionByEmail() {
		return actionByEmail;
	}
	/**
	 * @param actionByEmail the actionByEmail to set
	 */
	public void setActionByEmail(String actionByEmail) {
		this.actionByEmail = actionByEmail;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
	

}
