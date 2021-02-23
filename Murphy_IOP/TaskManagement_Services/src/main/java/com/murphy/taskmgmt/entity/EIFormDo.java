package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EI_FORM")
public class EIFormDo implements BaseDo, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "FORM_ID", length = 40)
	private String formId;
	
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "PERM_ISSUER_LOGIN_NAME")
	private String permIssueLoginName;
	
	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Column(name = "DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name = "PLANNED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date plannedDate;
	
	@Column(name = "LOCATION_ID")
	private String locationId;
	
	@Column(name = "LOCATION_NAME")
	private String locationName;
	
	@Column(name = "PERM_HOLD_NAME")
	private String permHoldName;
	
	@Column(name = "PERM_ISSUE_NAME")
	private String permIssueName;
	
	@Column(name = "CONTRACTOR_NAME")
	private String contractorName;
	
	@Column(name = "EXPECT_TIME")
	private Date expectTime;
	
	@Column(name = "PERM_ISSUE_TIME")
	private Date permIssueTime;
	
	@Column(name = "PERM_HOLD_TIME")
	private Date permHoldTime;
	
	@Column(name = "EQUIP_ID")
	private String equipId;
	
	@Column(name = "WORK_ORDER_NUM")
	private String workOrderNum;
	
	@Column(name = "ENERGY_TYPE")
	private String energyType;
	
	@Column(name = "REASON")
	private String reason;
	
	@Column(name = "OTHER_HAZARDS")
	private String otherHazards;
	
	@Column(name = "IS_EMP_NOTIFIED")
	private boolean isEmpNotified;
	
	@Column(name = "IS_LOTO_REMOVED")
	private boolean isLotoRemoved;
	
	@Column(name = "IS_LOCK_TAG_REMOVED")
	private boolean isLockTagRemoved;
	
	@Column(name = "IS_WA_INSPECTED")
	private boolean isWAInspected;
	
	@Column(name = "IS_JSA_INSPECTED")
	private boolean isJSAReviewed;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "USER_GROUP")
	private String userGroup;
	
	@Column(name = "IS_TEXT")
	private boolean isText;
	
	@Column(name = "IS_ACKNOWLEDGED")
	private boolean isAcknowledged;
	
	@Column(name = "SHIFT")
	private String shift;
	
	@Column(name = "LAST_SHIFT_CHANGE")
	private Date lastShiftChange;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getPermHoldName() {
		return permHoldName;
	}

	public void setPermHoldName(String permHoldName) {
		this.permHoldName = permHoldName;
	}

	public String getPermIssueName() {
		return permIssueName;
	}

	public void setPermIssueName(String permIssueName) {
		this.permIssueName = permIssueName;
	}

	public String getContractorName() {
		return contractorName;
	}
	
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public Date getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}

	public Date getPermIssueTime() {
		return permIssueTime;
	}

	public void setPermIssueTime(Date permIssueTime) {
		this.permIssueTime = permIssueTime;
	}

	public Date getPermHoldTime() {
		return permHoldTime;
	}

	public void setPermHoldTime(Date permHoldTime) {
		this.permHoldTime = permHoldTime;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getWorkOrderNum() {
		return workOrderNum;
	}

	public void setWorkOrderNum(String workOrderNum) {
		this.workOrderNum = workOrderNum;
	}

	public String getEnergyType() {
		return energyType;
	}

	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOtherHazards() {
		return otherHazards;
	}

	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}

	public boolean isEmpNotified() {
		return isEmpNotified;
	}

	public void setEmpNotified(boolean isEmpNotified) {
		this.isEmpNotified = isEmpNotified;
	}

	public boolean isLotoRemoved() {
		return isLotoRemoved;
	}

	public void setLotoRemoved(boolean isLotoRemoved) {
		this.isLotoRemoved = isLotoRemoved;
	}

	public boolean isLockTagRemoved() {
		return isLockTagRemoved;
	}

	public void setLockTagRemoved(boolean isLockTagRemoved) {
		this.isLockTagRemoved = isLockTagRemoved;
	}

	public boolean isWAInspected() {
		return isWAInspected;
	}

	public void setWAInspected(boolean isWAInspected) {
		this.isWAInspected = isWAInspected;
	}

	public boolean isJSAReviewed() {
		return isJSAReviewed;
	}

	public void setJSAReviewed(boolean isJSAReviewed) {
		this.isJSAReviewed = isJSAReviewed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
	public boolean isAcknowledged() {
		return isAcknowledged;
	}

	public void setAcknowledged(boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public String getPermIssueLoginName() {
		return permIssueLoginName;
	}

	public void setPermIssueLoginName(String permIssueLoginName) {
		this.permIssueLoginName = permIssueLoginName;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Date getLastShiftChange() {
		return lastShiftChange;
	}

	public void setLastShiftChange(Date lastShiftChange) {
		this.lastShiftChange = lastShiftChange;
	}

	@Override
	public Object getPrimaryKey() {
		return formId;
	}
}
