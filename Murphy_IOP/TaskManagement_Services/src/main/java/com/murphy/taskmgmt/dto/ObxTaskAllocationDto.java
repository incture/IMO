package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;

public class ObxTaskAllocationDto {
	
	private Integer clusterdId;
	private String well;
	private String tier;
	private String proOperator;
	private String proOperatorEmail;
	private Boolean isEditable;
	private String obxOperator;
	private String obxOperatorEmail;
	private String userUpdatedBy;
	private String locationCode;
	private Integer sequenceNum;
	private BigDecimal driveTime;
	private BigDecimal taskTime;
	private String field;
	private String day;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequenceNum;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
	/**
	 * @return the clusterdId
	 */
	public Integer getClusterdId() {
		return clusterdId;
	}
	/**
	 * @param clusterdId the clusterdId to set
	 */
	public void setClusterdId(Integer clusterdId) {
		this.clusterdId = clusterdId;
	}
	/**
	 * @return the well
	 */
	public String getWell() {
		return well;
	}
	/**
	 * @param well the well to set
	 */
	public void setWell(String well) {
		this.well = well;
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
	 * @return the obxOperator
	 */
	public String getObxOperator() {
		return obxOperator;
	}
	/**
	 * @param obxOperator the obxOperator to set
	 */
	public void setObxOperator(String obxOperator) {
		this.obxOperator = obxOperator;
	}
	/**
	 * @return the proOperator
	 */
	public String getProOperator() {
		return proOperator;
	}
	/**
	 * @param proOperator the proOperator to set
	 */
	public void setProOperator(String proOperator) {
		this.proOperator = proOperator;
	}
	/**
	 * @return the isEditable
	 */
	public Boolean getIsEditable() {
		return isEditable;
	}
	/**
	 * @param isEditable the isEditable to set
	 */
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}
	/**
	 * @return the userUpdatedBy
	 */
	public String getUserUpdatedBy() {
		return userUpdatedBy;
	}
	/**
	 * @param userUpdatedBy the userUpdatedBy to set
	 */
	public void setUserUpdatedBy(String userUpdatedBy) {
		this.userUpdatedBy = userUpdatedBy;
	}
	/**
	 * @return the proOperatorEmail
	 */
	public String getProOperatorEmail() {
		return proOperatorEmail;
	}
	/**
	 * @param proOperatorEmail the proOperatorEmail to set
	 */
	public void setProOperatorEmail(String proOperatorEmail) {
		this.proOperatorEmail = proOperatorEmail;
	}
	/**
	 * @return the obxOperatorEmail
	 */
	public String getObxOperatorEmail() {
		return obxOperatorEmail;
	}
	/**
	 * @param obxOperatorEmail the obxOperatorEmail to set
	 */
	public void setObxOperatorEmail(String obxOperatorEmail) {
		this.obxOperatorEmail = obxOperatorEmail;
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
	 * @return the driveTime
	 */
	public BigDecimal getDriveTime() {
		return driveTime;
	}
	/**
	 * @param driveTime the driveTime to set
	 */
	public void setDriveTime(BigDecimal driveTime) {
		this.driveTime = driveTime;
	}
	/**
	 * @return the taskTime
	 */
	public BigDecimal getTaskTime() {
		return taskTime;
	}
	/**
	 * @param taskTime the taskTime to set
	 */
	public void setTaskTime(BigDecimal taskTime) {
		this.taskTime = taskTime;
	}
	public Integer getSequenceNum() {
		return sequenceNum;
	}
	public void setSequenceNum(Integer sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	@Override
	public String toString() {
		return "ObxTaskAllocationDto [clusterdId=" + clusterdId + ", well=" + well + ", tier=" + tier + ", proOperator="
				+ proOperator + ", proOperatorEmail=" + proOperatorEmail + ", isEditable=" + isEditable
				+ ", obxOperator=" + obxOperator + ", obxOperatorEmail=" + obxOperatorEmail + ", userUpdatedBy="
				+ userUpdatedBy + ", locationCode=" + locationCode + ", sequenceNum=" + sequenceNum + ", driveTime="
				+ driveTime + ", taskTime=" + taskTime + ", field=" + field + ", day=" + day + ", status=" + status
				+ "]";
	}
	
	
}
