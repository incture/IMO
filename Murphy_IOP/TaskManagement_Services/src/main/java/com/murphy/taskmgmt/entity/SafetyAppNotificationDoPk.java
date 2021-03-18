package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SafetyAppNotificationDoPk implements Serializable{

	private static final long serialVersionUID = 2713178264137136552L;

	
	@Column(name = "OBJECT_ID", length = 32)
	private String objectId;

	@Column(name = "ACTIVITY_LOG_ID", length = 32)
	private String activityLogId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getActivityLogId() {
		return activityLogId;
	}

	public void setActivityLogId(String activityLogId) {
		this.activityLogId = activityLogId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BypassNotificationDoPk [objectId=" + objectId + ", activityLogId=" + activityLogId + "]";
	}
	
	
}
