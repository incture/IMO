package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class NotificationDto extends BaseDto  {

	private String objectId;
	private String module;
	private String locationText;
	private Date acknowledgedAt;
	private String isAcknowledged;
	private String userGroup;
	private String userId;
	private String assignedTo;
	private String status;
	private String subject;
	private String facilityDesc;
	private String longDesc;
	private String activityLogId;
	private String safetyAppMsg;
	private String locationCode;
	private String locationType;
	private String severity;
	private String bypassNum;
	
	
	
	
	
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
	public String getLocationText() {
		return locationText;
	}
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	public Date getAcknowledgedAt() {
		return acknowledgedAt;
	}
	public void setAcknowledgedAt(Date acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}
	public String getIsAcknowledged() {
		return isAcknowledged;
	}
	public void setIsAcknowledged(String isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActivityLogId() {
		return activityLogId;
	}
	public void setActivityLogId(String activityLogId) {
		this.activityLogId = activityLogId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFacilityDesc() {
		return facilityDesc;
	}
	public void setFacilityDesc(String facilityDesc) {
		this.facilityDesc = facilityDesc;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	
	
	
	public String getSafetyAppMsg() {
		return safetyAppMsg;
	}
	public void setSafetyAppMsg(String safetyAppMsg) {
		this.safetyAppMsg = safetyAppMsg;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getBypassNum() {
		return bypassNum;
	}
	public void setBypassNum(String bypassNum) {
		this.bypassNum = bypassNum;
	}
	@Override
	public String toString() {
		return "NotificationDto [objectId=" + objectId + ", module=" + module + ", locationText=" + locationText
				+ ", acknowledgedAt=" + acknowledgedAt + ", isAcknowledged=" + isAcknowledged + ", userGroup="
				+ userGroup + ", userId=" + userId + ", assignedTo=" + assignedTo + ", status=" + status + ", subject="
				+ subject + ", facilityDesc=" + facilityDesc + ", longDesc=" + longDesc + ", activityLogId="
				+ activityLogId + ", safetyAppMsg=" + safetyAppMsg + ", locationCode=" + locationCode
				+ ", locationType=" + locationType + ", severity=" + severity + ", bypassNum=" + bypassNum + "]";
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
