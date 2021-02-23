package com.murphy.taskmgmt.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OBX_TASK_ALLOCATION_DUMMY")
public class ObxTaskDoDummy implements BaseDo{


	@EmbeddedId
	private ObxTaskDoPKDummy obxTaskDoPK;
	
	@Column(name="TASK_OWNER_EMAIL",length=60)
	private String ownerEmail;
	
	@Column(name="FIELD",length=80)
	private String field;
	
	@Column(name="LOCATION_TEXT",length=80)
	private String locationText;
	
	@Column(name="TIER",length=10)
	private String tier;
	
	@Column(name = "LONGITUDE")
	private BigDecimal longitude;
	
	@Column(name = "LATITUDE")
	private BigDecimal latitude;
	
	@Column(name = "SEQUENCE_NUMBER")
	private int sequnceNumber;

	@Column(name = "DRIVE_TIME")
	private BigDecimal driveTime;
	
	@Column(name = "ESTIMATED_TASK_TIME")
	private BigDecimal estimatedTaskTime;
	
	@Column(name = "ROLE")
	private String role;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	

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


	public BigDecimal getLongitude() {
		return longitude;
	}


	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}


	public BigDecimal getLatitude() {
		return latitude;
	}


	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}


	public int getSequnceNumber() {
		return sequnceNumber;
	}


	public void setSequnceNumber(int sequnceNumber) {
		this.sequnceNumber = sequnceNumber;
	}

	@Override
	public String toString() {
		return "ObxTaskDo [obxTaskDoPK=" + obxTaskDoPK + ", ownerEmail=" + ownerEmail + ", field=" + field
				+ ", locationText=" + locationText + ", tier=" + tier + ", longitude=" + longitude + ", latitude="
				+ latitude + ", sequnceNumber=" + sequnceNumber + ", driveTime=" + driveTime + ", estimatedTaskTime="
				+ estimatedTaskTime + ", role=" + role + ", updatedBy=" + updatedBy + "]";
	}


	public BigDecimal getDriveTime() {
		return driveTime;
	}


	public void setDriveTime(BigDecimal driveTime) {
		this.driveTime = driveTime;
	}

	public BigDecimal getEstimatedTaskTime() {
		return estimatedTaskTime;
	}


	public void setEstimatedTaskTime(BigDecimal estimatedTaskTime) {
		this.estimatedTaskTime = estimatedTaskTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public ObxTaskDoPKDummy getObxTaskDoPK() {
		return obxTaskDoPK;
	}


	public void setObxTaskDoPK(ObxTaskDoPKDummy obxTaskDoPK) {
		this.obxTaskDoPK = obxTaskDoPK;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	@Override
	public Object getPrimaryKey() {
		return obxTaskDoPK;
	}
	
}
