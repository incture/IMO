package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class SsdBypassHeaderDto extends BaseDto {
	private String ssdBypassId;
	private String ssdBypassNum;
	private String deviceBypassed;
	private String reasonForBypass;
	private String bypassStartedBy;
	private Date bypassStartTime;
	private Date bypassCompleteTime;
	private Boolean isOpApprovalObtained;
	private Boolean isBypassTagAttached;
	private Boolean isDcsPlcControlledDevice;
	private Boolean isAffectedPersonnelNotified;
	private String firstLineSupvApprovalStatus;
	private Date firstLineSupvApprovalInitAt;
	private String firstLineSupvApprovalInitBy;
	private String fieldPlantSuptApprovalStatus;
	private Date fieldPlantSuptApprovalInitAt;
	private String fieldPlantSuptApprovalInitBy;
	private String opMgrApprovalStatus;
	private Date OpMgrApprovalInitAt;
	private String OpMgrApprovalInitBy;
	private Boolean isEmocCreated;
	private String emocNumber;
	private String bypassStatus;
	private String operatorId;
	private String severity;
	private String locationCode;
	private String location;
	private Boolean shiftChangeAcknowledged;
	private String assignedTo;
	private String equipmentId;
	private String equipmentDesc;
	private String risk;

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentDesc() {
		return equipmentDesc;
	}

	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public String getRisk() {
		return risk;
	}

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
	 * @return the ssdBypassCommentDtoList
	 */
	/*
	 * public List<SsdBypassCommentDto> getSsdBypassCommentDtoList() { return
	 * ssdBypassCommentDtoList; }
	 * 
	 *//**
		 * @param ssdBypassCommentDtoList
		 *            the ssdBypassCommentDtoList to set
		 */
	/*
	 * public void setSsdBypassCommentDtoList(List<SsdBypassCommentDto>
	 * ssdBypassCommentDtoList) { this.ssdBypassCommentDtoList =
	 * ssdBypassCommentDtoList; }
	 * 
	 *//**
		 * @return the ssdBypassAttachementDtoList
		 */
	/*
	 * public List<SsdBypassAttachementDto> getSsdBypassAttachementDtoList() {
	 * return ssdBypassAttachementDtoList; }
	 * 
	 *//**
		 * @param ssdBypassAttachementDtoList
		 *            the ssdBypassAttachementDtoList to set
		 */
	/*
	 * public void setSsdBypassAttachementDtoList(List<SsdBypassAttachementDto>
	 * ssdBypassAttachementDtoList) { this.ssdBypassAttachementDtoList =
	 * ssdBypassAttachementDtoList; }
	 * 
	 *//**
		 * @return the ssdBypassActivityLogDtoList
		 */

	/*
	 * public List<SsdBypassActivityLogDto> getSsdBypassActivityLogDtoList() {
	 * return ssdBypassActivityLogDtoList; }
	 * 
	 *//**
		 * @param ssdBypassActivityLogDtoList
		 *            the ssdBypassActivityLogDtoList to set
		 *//*
		 * public void
		 * setSsdBypassActivityLogDtoList(List<SsdBypassActivityLogDto>
		 * ssdBypassActivityLogDtoList) { this.ssdBypassActivityLogDtoList =
		 * ssdBypassActivityLogDtoList; }
		 */

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {

		// TODO Auto-generated method stub

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
	 * @param operatorId
	 *            the operatorId to set
	 */
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}

	/**
	 * @param locationCode
	 *            the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
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

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	

}
