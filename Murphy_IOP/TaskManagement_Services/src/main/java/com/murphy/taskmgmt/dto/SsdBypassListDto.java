package com.murphy.taskmgmt.dto;

import java.util.Date;

public class SsdBypassListDto {
	private String ssdBypassId;
	private String ssdBypassNum;
	private String deviceBypassed;
	private String reasonForBypass;
	private Date bypassStartTime;
	private String bypassStatus;
	private String location;
	private String locationCode;
	private String assignedTo;
	private String source;
	private String equipment_desc;
	
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
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo
	 *            the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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

}
