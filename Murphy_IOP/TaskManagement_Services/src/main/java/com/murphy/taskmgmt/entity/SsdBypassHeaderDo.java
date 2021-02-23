package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SSD_BYPASS_HEADER")
public class SsdBypassHeaderDo implements BaseDo {

	@Id
	@Column(name = "SSD_BYPASS_ID", length = 100)
	private String ssdBypassId;

	@Column(name = "SSD_BYPASS_NUMBER", length = 100)
	private String ssdBypassNum;

	@Column(name = "DEVICE_BYPASSED", length = 100)
	private String deviceBypassed;

	@Column(name = "REASON_FOR_BYPASS", length = 1000)
	private String reasonForBypass;

	@Column(name = "BYPASS_STARTED_BY", length = 100)
	private String bypassStartedBy;

	@Column(name = "BYPASS_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bypassStartTime;

	@Column(name = "BYPASS_COMPLETE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bypassCompleteTime;

	@Column(name = "IS_OPERATOR_APPROVAL_OBTAINED")
	private Boolean isOpApprovalObtained;

	@Column(name = "IS_BYPASS_TAG_ATTACHED")
	private Boolean isBypassTagAttached;

	@Column(name = "IS_DCS_PLC_CONTROLLED_DEVICE")
	private Boolean isDcsPlcControlledDevice;

	@Column(name = "IS_AFFECTED_PERSONNEL_NOTIFIED")
	private Boolean isAffectedPersonnelNotified;

	@Column(name = "FIRST_LINE_SUPV_APPROVAL_STATUS")
	private String firstLineSupvApprovalStatus;

	@Column(name = "FIRST_LINE_SUPV_APPROVAL_INIT_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstLineSupvApprovalInitAt;

	@Column(name = "FIRST_LINE_SUPV_APPROVAL_INIT_BY", length = 100)
	private String firstLineSupvApprovalInitBy;

	@Column(name = "FIELD_PLANT_SUPT_APPROVAL_STATUS")
	private String fieldPlantSuptApprovalStatus;

	@Column(name = "FIELD_PLANT_SUPT_APPROVAL_INIT_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fieldPlantSuptApprovalInitAt;

	@Column(name = "FIELD_PLANT_SUPT_APPROVAL_INIT_BY", length = 100)
	private String fieldPlantSuptApprovalInitBy;

	@Column(name = "OP_MGR_APPROVAL_STATUS")
	private String opMgrApprovalStatus;

	@Column(name = "OP_MGR_APPROVAL_INIT_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date OpMgrApprovalInitAt;

	@Column(name = "OP_MGR_APPROVAL_INIT_BY", length = 100)
	private String OpMgrApprovalInitBy;

	@Column(name = "IS_EMOC_CREATED")
	private Boolean isEmocCreated;

	@Column(name = "EMOC_NUMBER", length = 100)
	private String emocNumber;

	@Column(name = "BYPASS_STATUS", length = 100)
	private String bypassStatus;

	@Column(name = "LOCATION", length = 100)
	private String location;

	@Column(name = "OPERATOR_ID", length = 50)
	private String operatorId;

	@Column(name = "SEVERITY", length = 50)
	private String severity;

	@Column(name = "LOCATION_CODE", length = 50)
	private String locationCode;

	@Column(name = "SHIFT_CHANGE_ACKNOWLEDGED")
	private Boolean shiftChangeAcknowledged;
	
	@Column(name = "EQUIPMENT_ID", length = 300)
	private String equipment_id;
	
	@Column(name = "EQUIPMENT_DESC", length = 300)
	private String equipment_desc;
	
	@Column(name = "RISK", length = 100)
	private String risk;
	
	/**
	 * @return the equipment_id
	 */
	public String getEquipment_id() {
		return equipment_id;
	}

	/**
	 * @param equipment_id the equipment_id to set
	 */
	public void setEquipment_id(String equipment_id) {
		this.equipment_id = equipment_id;
	}

	/**
	 * @return the equipment_desc
	 */
	public String getEquipment_desc() {
		return equipment_desc;
	}

	/**
	 * @param equipment_desc the equipment_desc to set
	 */
	public void setEquipment_desc(String equipment_desc) {
		this.equipment_desc = equipment_desc;
	}

	/**
	 * @return the risk
	 */
	public String getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public void setRisk(String risk) {
		this.risk = risk;
	}

	/**
	 * @return the ssdBypassId
	 */
	public String getSsdBypassId() {
		return ssdBypassId;
	}

	/**
	 * @param ssdBypassId
	 *            the ssdBypassId to set
	 */
	public void setSsdBypassId(String ssdBypassId) {
		this.ssdBypassId = ssdBypassId;
	}

	/**
	 * @return the ssdBypassNum
	 */
	public String getSsdBypassNum() {
		return ssdBypassNum;
	}

	/**
	 * @param ssdBypassNum
	 *            the ssdBypassNum to set
	 */
	public void setSsdBypassNum(String ssdBypassNum) {
		this.ssdBypassNum = ssdBypassNum;
	}

	/**
	 * @return the deviceBypassed
	 */
	public String getDeviceBypassed() {
		return deviceBypassed;
	}

	/**
	 * @param deviceBypassed
	 *            the deviceBypassed to set
	 */
	public void setDeviceBypassed(String deviceBypassed) {
		this.deviceBypassed = deviceBypassed;
	}

	/**
	 * @return the reasonForBypass
	 */
	public String getReasonForBypass() {
		return reasonForBypass;
	}

	/**
	 * @param reasonForBypass
	 *            the reasonForBypass to set
	 */
	public void setReasonForBypass(String reasonForBypass) {
		this.reasonForBypass = reasonForBypass;
	}

	/**
	 * @return the bypassStartedBy
	 */
	public String getBypassStartedBy() {
		return bypassStartedBy;
	}

	/**
	 * @param bypassStartedBy
	 *            the bypassStartedBy to set
	 */
	public void setBypassStartedBy(String bypassStartedBy) {
		this.bypassStartedBy = bypassStartedBy;
	}

	/**
	 * @return the bypassStartTime
	 */
	public Date getBypassStartTime() {
		return bypassStartTime;
	}

	/**
	 * @param bypassStartTime
	 *            the bypassStartTime to set
	 */
	public void setBypassStartTime(Date bypassStartTime) {
		this.bypassStartTime = bypassStartTime;
	}

	/**
	 * @return the bypassCompleteTime
	 */
	public Date getBypassCompleteTime() {
		return bypassCompleteTime;
	}

	/**
	 * @param bypassCompleteTime
	 *            the bypassCompleteTime to set
	 */
	public void setBypassCompleteTime(Date bypassCompleteTime) {
		this.bypassCompleteTime = bypassCompleteTime;
	}

	/**
	 * @return the isOpApprovalObtained
	 */
	public Boolean getIsOpApprovalObtained() {
		return isOpApprovalObtained;
	}

	/**
	 * @param isOpApprovalObtained
	 *            the isOpApprovalObtained to set
	 */
	public void setIsOpApprovalObtained(Boolean isOpApprovalObtained) {
		this.isOpApprovalObtained = isOpApprovalObtained;
	}

	/**
	 * @return the isBypassTagAttached
	 */
	public Boolean getIsBypassTagAttached() {
		return isBypassTagAttached;
	}

	/**
	 * @param isBypassTagAttached
	 *            the isBypassTagAttached to set
	 */
	public void setIsBypassTagAttached(Boolean isBypassTagAttached) {
		this.isBypassTagAttached = isBypassTagAttached;
	}

	/**
	 * @return the isDcsPlcControlledDevice
	 */
	public Boolean getIsDcsPlcControlledDevice() {
		return isDcsPlcControlledDevice;
	}

	/**
	 * @param isDcsPlcControlledDevice
	 *            the isDcsPlcControlledDevice to set
	 */
	public void setIsDcsPlcControlledDevice(Boolean isDcsPlcControlledDevice) {
		this.isDcsPlcControlledDevice = isDcsPlcControlledDevice;
	}

	/**
	 * @return the isAffectedPersonnelNotified
	 */
	public Boolean getIsAffectedPersonnelNotified() {
		return isAffectedPersonnelNotified;
	}

	/**
	 * @param isAffectedPersonnelNotified
	 *            the isAffectedPersonnelNotified to set
	 */
	public void setIsAffectedPersonnelNotified(Boolean isAffectedPersonnelNotified) {
		this.isAffectedPersonnelNotified = isAffectedPersonnelNotified;
	}

	/**
	 * @return the firstLineSupvApprovalStatus
	 */
	public String getFirstLineSupvApprovalStatus() {
		return firstLineSupvApprovalStatus;
	}

	/**
	 * @param firstLineSupvApprovalStatus
	 *            the firstLineSupvApprovalStatus to set
	 */
	public void setFirstLineSupvApprovalStatus(String firstLineSupvApprovalStatus) {
		this.firstLineSupvApprovalStatus = firstLineSupvApprovalStatus;
	}

	/**
	 * @return the firstLineSupvApprovalInitAt
	 */
	public Date getFirstLineSupvApprovalInitAt() {
		return firstLineSupvApprovalInitAt;
	}

	/**
	 * @param firstLineSupvApprovalInitAt
	 *            the firstLineSupvApprovalInitAt to set
	 */
	public void setFirstLineSupvApprovalInitAt(Date firstLineSupvApprovalInitAt) {
		this.firstLineSupvApprovalInitAt = firstLineSupvApprovalInitAt;
	}

	/**
	 * @return the firstLineSupvApprovalInitBy
	 */
	public String getFirstLineSupvApprovalInitBy() {
		return firstLineSupvApprovalInitBy;
	}

	/**
	 * @param firstLineSupvApprovalInitBy
	 *            the firstLineSupvApprovalInitBy to set
	 */
	public void setFirstLineSupvApprovalInitBy(String firstLineSupvApprovalInitBy) {
		this.firstLineSupvApprovalInitBy = firstLineSupvApprovalInitBy;
	}

	/**
	 * @return the fieldPlantSuptApprovalStatus
	 */
	public String getFieldPlantSuptApprovalStatus() {
		return fieldPlantSuptApprovalStatus;
	}

	/**
	 * @param fieldPlantSuptApprovalStatus
	 *            the fieldPlantSuptApprovalStatus to set
	 */
	public void setFieldPlantSuptApprovalStatus(String fieldPlantSuptApprovalStatus) {
		this.fieldPlantSuptApprovalStatus = fieldPlantSuptApprovalStatus;
	}

	/**
	 * @return the fieldPlantSuptApprovalInitAt
	 */
	public Date getFieldPlantSuptApprovalInitAt() {
		return fieldPlantSuptApprovalInitAt;
	}

	/**
	 * @param fieldPlantSuptApprovalInitAt
	 *            the fieldPlantSuptApprovalInitAt to set
	 */
	public void setFieldPlantSuptApprovalInitAt(Date fieldPlantSuptApprovalInitAt) {
		this.fieldPlantSuptApprovalInitAt = fieldPlantSuptApprovalInitAt;
	}

	/**
	 * @return the fieldPlantSuptApprovalInitBy
	 */
	public String getFieldPlantSuptApprovalInitBy() {
		return fieldPlantSuptApprovalInitBy;
	}

	/**
	 * @param fieldPlantSuptApprovalInitBy
	 *            the fieldPlantSuptApprovalInitBy to set
	 */
	public void setFieldPlantSuptApprovalInitBy(String fieldPlantSuptApprovalInitBy) {
		this.fieldPlantSuptApprovalInitBy = fieldPlantSuptApprovalInitBy;
	}

	/**
	 * @return the opMgrApprovalStatus
	 */
	public String getOpMgrApprovalStatus() {
		return opMgrApprovalStatus;
	}

	/**
	 * @param opMgrApprovalStatus
	 *            the opMgrApprovalStatus to set
	 */
	public void setOpMgrApprovalStatus(String opMgrApprovalStatus) {
		this.opMgrApprovalStatus = opMgrApprovalStatus;
	}

	/**
	 * @return the opMgrApprovalInitAt
	 */
	public Date getOpMgrApprovalInitAt() {
		return OpMgrApprovalInitAt;
	}

	/**
	 * @param opMgrApprovalInitAt
	 *            the opMgrApprovalInitAt to set
	 */
	public void setOpMgrApprovalInitAt(Date opMgrApprovalInitAt) {
		OpMgrApprovalInitAt = opMgrApprovalInitAt;
	}

	/**
	 * @return the opMgrApprovalInitBy
	 */
	public String getOpMgrApprovalInitBy() {
		return OpMgrApprovalInitBy;
	}

	/**
	 * @param opMgrApprovalInitBy
	 *            the opMgrApprovalInitBy to set
	 */
	public void setOpMgrApprovalInitBy(String opMgrApprovalInitBy) {
		OpMgrApprovalInitBy = opMgrApprovalInitBy;
	}

	/**
	 * @return the isEmocCreated
	 */
	public Boolean getIsEmocCreated() {
		return isEmocCreated;
	}

	/**
	 * @param isEmocCreated
	 *            the isEmocCreated to set
	 */
	public void setIsEmocCreated(Boolean isEmocCreated) {
		this.isEmocCreated = isEmocCreated;
	}

	/**
	 * @return the emocNumber
	 */
	public String getEmocNumber() {
		return emocNumber;
	}

	/**
	 * @param emocNumber
	 *            the emocNumber to set
	 */
	public void setEmocNumber(String emocNumber) {
		this.emocNumber = emocNumber;
	}

	/**
	 * @return the bypassStatus
	 */
	public String getBypassStatus() {
		return bypassStatus;
	}

	/**
	 * @param bypassStatus
	 *            the bypassStatus to set
	 */
	public void setBypassStatus(String bypassStatus) {
		this.bypassStatus = bypassStatus;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the operatorId
	 */
	public String getOperatorId() {
		return operatorId;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * @param operatorId
	 *            the operatorId to set
	 */
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}

	/**
	 * @return the shiftChangeAcknowledged
	 */
	public Boolean getShiftChangeAcknowledged() {
		return shiftChangeAcknowledged;
	}

	/**
	 * @param shiftChangeAcknowledged
	 *            the shiftChangeAcknowledged to set
	 */
	public void setShiftChangeAcknowledged(Boolean shiftChangeAcknowledged) {
		this.shiftChangeAcknowledged = shiftChangeAcknowledged;
	}

	/**
	 * @param locationCode
	 *            the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return ssdBypassId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SsdBypassHeaderDo [ssdBypassId=" + ssdBypassId + ", ssdBypassNum=" + ssdBypassNum + ", deviceBypassed="
				+ deviceBypassed + ", reasonForBypass=" + reasonForBypass + ", bypassStartedBy=" + bypassStartedBy
				+ ", bypassStartTime=" + bypassStartTime + ", bypassCompleteTime=" + bypassCompleteTime
				+ ", isOpApprovalObtained=" + isOpApprovalObtained + ", isBypassTagAttached=" + isBypassTagAttached
				+ ", isDcsPlcControlledDevice=" + isDcsPlcControlledDevice + ", isAffectedPersonnelNotified="
				+ isAffectedPersonnelNotified + ", firstLineSupvApprovalStatus=" + firstLineSupvApprovalStatus
				+ ", firstLineSupvApprovalInitAt=" + firstLineSupvApprovalInitAt + ", firstLineSupvApprovalInitBy="
				+ firstLineSupvApprovalInitBy + ", fieldPlantSuptApprovalStatus=" + fieldPlantSuptApprovalStatus
				+ ", fieldPlantSuptApprovalInitAt=" + fieldPlantSuptApprovalInitAt + ", fieldPlantSuptApprovalInitBy="
				+ fieldPlantSuptApprovalInitBy + ", opMgrApprovalStatus=" + opMgrApprovalStatus
				+ ", OpMgrApprovalInitAt=" + OpMgrApprovalInitAt + ", OpMgrApprovalInitBy=" + OpMgrApprovalInitBy
				+ ", isEmocCreated=" + isEmocCreated + ", emocNumber=" + emocNumber + ", bypassStatus=" + bypassStatus
				+ ", location=" + location + ", operatorId=" + operatorId + ", severity=" + severity + ", locationCode="
				+ locationCode + ", shiftChangeAcknowledged=" + shiftChangeAcknowledged + ", equipment_id="
				+ equipment_id + ", equipment_desc=" + equipment_desc + ", risk=" + risk + "]";
	}

}
