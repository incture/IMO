package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "SHIFT_AUDIT_LOG")
public class ShiftAuditLogDo implements BaseDo, Serializable  {

	
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "SHIFT_AUDIT_ID")
	private String shiftAuditId;
	

	@Column(name = "RESOURCE")
	private String resource;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;
	
	@Column(name = "CURRENT_SHIFT")
	private String currentShift;
	
	@Column(name = "PREVIOUS_SHIFT")
	private String previousShift;
	
	@Column(name = "BASE_LOC")
	private String currentBaseLocation;
	
	@Column(name = "PREV_BASE_LOC")
	private String previousBaseLocation;

	@Column(name = "SHIFT_DAY")
	private String shiftDay;
	
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getShiftAuditId() {
		return shiftAuditId;
	}

	public void setShiftAuditId(String shiftAuditId) {
		this.shiftAuditId = shiftAuditId;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getCurrentShift() {
		return currentShift;
	}

	public void setCurrentShift(String currentShift) {
		this.currentShift = currentShift;
	}

	public String getPreviousShift() {
		return previousShift;
	}

	public void setPreviousShift(String previousShift) {
		this.previousShift = previousShift;
	}

	public String getBaseLocation() {
		return currentBaseLocation;
	}

	public void setBaseLocation(String baseLocation) {
		this.currentBaseLocation = baseLocation;
	}

	public String getPreviousBaseLocation() {
		return previousBaseLocation;
	}

	public void setPreviousBaseLocation(String previousBaseLocation) {
		this.previousBaseLocation = previousBaseLocation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrentBaseLocation() {
		return currentBaseLocation;
	}

	public void setCurrentBaseLocation(String currentBaseLocation) {
		this.currentBaseLocation = currentBaseLocation;
	}

	public String getShiftDay() {
		return shiftDay;
	}

	public void setShiftDay(String shiftDay) {
		this.shiftDay = shiftDay;
	}

	@Override
	public String toString() {
		return "ShiftAuditLogDo [shiftAuditId=" + shiftAuditId + ", resource=" + resource + ", modifiedBy=" + modifiedBy
				+ ", modifiedAt=" + modifiedAt + ", currentShift=" + currentShift + ", previousShift=" + previousShift
				+ ", currentBaseLocation=" + currentBaseLocation + ", previousBaseLocation=" + previousBaseLocation
				+ ", shiftDay=" + shiftDay + "]";
	}

}
