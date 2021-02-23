package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NOTIFICATION_DETAILS")
public class NotificationDo implements BaseDo, Serializable  {

	private static final long serialVersionUID = 7318165884730641958L;
	
	
	@EmbeddedId
	private NotificationDoPK notificationDoPk;
		
	@Column(name = "MODULE", length = 50)
	private String module;
	
	@Column(name = "LOCATION_TEXT",length=100)
	private String locationtext;
	
	@Column(name = "USER_GROUP",length = 200)
	private String userGroup;
	
	@Column(name="ACKNOWLEDGED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date acknowledgedAt;
	
	@Column(name = "IS_ACKNOWLEDGED")
	private String isAcknowledged;
	
	@Column(name = "WELL_STATUS",length =50)
	private String wellStatus;
	
	@Column(name = "LOC_CODE",length =100)
	private String locCode;
	
	@Column(name = "LOC_TYPE",length =50)
	private String locType;

	

	public NotificationDoPK getNotificationDoPk() {
		return notificationDoPk;
	}


	public void setNotificationDoPk(NotificationDoPK notificationDoPk) {
		this.notificationDoPk = notificationDoPk;
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


	public String getWellStatus() {
		return wellStatus;
	}


	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}


	
	
	public String getLocCode() {
		return locCode;
	}


	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}


	public String getLocType() {
		return locType;
	}


	public void setLocType(String locType) {
		this.locType = locType;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toString() {
		return "NotificationDo [notificationDoPk=" + notificationDoPk + ", module=" + module + ", locationtext="
				+ locationtext + ", userGroup=" + userGroup + ", acknowledgedAt=" + acknowledgedAt + ", isAcknowledged="
				+ isAcknowledged + ", wellStatus=" + wellStatus + ", locCode=" + locCode + ", locType=" + locType + "]";
	}


	
	
	
	
}
