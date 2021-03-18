package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SAFETY_APP_NOTIFICATION")
public class SafetyAppNotificationDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 7318165884730641958L;

	@EmbeddedId
	private SafetyAppNotificationDoPk bypassNotificationDoPk;

	@Column(name = "MODULE", length = 50)
	private String module;

	@Column(name = "LOCATION_TEXT", length = 50)
	private String locationtext;

	@Column(name = "USER_GROUP", length = 200)
	private String userGroup;

	@Column(name = "USER_ID", length = 50)
	private String userId;

	@Column(name = "ACKNOWLEDGED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date acknowledgedAt;

	@Column(name = "IS_ACKNOWLEDGED")
	private String isAcknowledged;

	@Column(name = "STATUS", length = 20)
	private String status;

	@Column(name = "ACKNOWLEDGED_BY", length = 100)
	private String acknowledgedBy;

	@Column(name = "LOCATION_CODE", length = 100)
	private String locationCode;

	@Column(name = "LOCATION_TYPE", length = 50)
	private String locationType;

	@Column(name = "SEVERITY", length = 20)
	private String severityType;

	@Column(name = "BYPASS_NUM", length = 100)
	private String bypassNum;

	public SafetyAppNotificationDoPk getBypassNotificationDoPk() {
		return bypassNotificationDoPk;
	}

	public void setBypassNotificationDoPk(SafetyAppNotificationDoPk bypassNotificationDoPk) {
		this.bypassNotificationDoPk = bypassNotificationDoPk;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getLocationtext() {
		return locationtext;
	}

	public void setLocationtext(String locationtext) {
		this.locationtext = locationtext;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcknowledgedBy() {
		return acknowledgedBy;
	}

	public void setAcknowledgedBy(String acknowledgedBy) {
		this.acknowledgedBy = acknowledgedBy;
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

	public String getSeverityType() {
		return severityType;
	}

	public void setSeverityType(String severityType) {
		this.severityType = severityType;
	}

	public String getBypassNum() {
		return bypassNum;
	}

	public void setBypassNum(String bypassNum) {
		this.bypassNum = bypassNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SafetyAppNotificationDo [bypassNotificationDoPk=" + bypassNotificationDoPk + ", module=" + module
				+ ", locationtext=" + locationtext + ", userGroup=" + userGroup + ", userId=" + userId
				+ ", acknowledgedAt=" + acknowledgedAt + ", isAcknowledged=" + isAcknowledged + ", status=" + status
				+ ", acknowledgedBy=" + acknowledgedBy + ", locationCode=" + locationCode + ", locationType="
				+ locationType + ", severityType=" + severityType + ", bypassNum=" + bypassNum + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
