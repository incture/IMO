package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ObxTaskDto extends BaseDto{

	private String locationCode;
	private String ownerEmail;
	private String field;
	private String locationText;
	private String tier;
	private Double latitude;
	private Double longitude;
	private int sequenceNumber;
	private Double driveTime;
	private String updatedBy;
	private int clusterNumber;
	private String role;
	private int day;
	private double estimatedTaskTime;
	private String isObxOperator;
	private String status;

	public double getEstimatedTaskTime() {
		return estimatedTaskTime;
	}

	public void setEstimatedTaskTime(double estimatedTaskTime) {
		this.estimatedTaskTime = estimatedTaskTime;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Double getDriveTime() {
		return driveTime;
	}

	public void setDriveTime(Double driveTime) {
		this.driveTime = driveTime;
	}

	@Override
	public Boolean getValidForUsage() {
		return true;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getClusterNumber() {
		return clusterNumber;
	}

	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "ObxTaskDto [locationCode=" + locationCode + ", ownerEmail=" + ownerEmail + ", field=" + field
				+ ", locationText=" + locationText + ", tier=" + tier + ", latitude=" + latitude + ", longitude="
				+ longitude + ", sequenceNumber=" + sequenceNumber + ", driveTime=" + driveTime + ", updatedBy="
				+ updatedBy + ", clusterNumber=" + clusterNumber + ", role=" + role + ", day=" + day
				+ ", estimatedTaskTime=" + estimatedTaskTime + ", isObxOperator=" + isObxOperator + "]";
	}

	public String getIsObxOperator() {
		return isObxOperator;
	}

	public void setIsObxOperator(String isObxOperator) {
		this.isObxOperator = isObxOperator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
