package com.murphy.downtime.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonAutoDetect
public class DowntimeCaptureDto {

	private String uwiId;
//	private int objectType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date originalDateEntered;
	private int durationInHours;
	private int durationInMinutes;
	private String parentCode;
	private String childCode;
//	private int upDownFlag;
//	private int dateEntryFlag;
//	private float repairCosts;
//	private float lostProduction;
//	private int calCDowntimeFlag;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date startDate;
//	private Date endDate;
//	private Date startProductionDate;
//	private Date endProducationDate;
	private String comments;
//	private String reason;
//	private int messageSendFlag;
//	private int destinationPerson;
//	private String rioDowntimeId;
//	private float lastDayHoursDown;
//	private int deleteFlag;
//	private int lastTransmission;
//	private Date lastLoadDate;
//	private int transmitFlag;
//	private Date dateTimeStamp;
//	private Date bLogicDateStamp;
//	private Date userDateStamp;
//	private String userId;

//	public int getUpDownFlag() {
//		return upDownFlag;
//	}
//
//	public void setUpDownFlag(int upDownFlag) {
//		this.upDownFlag = upDownFlag;
//	}
//
//	public int getDateEntryFlag() {
//		return dateEntryFlag;
//	}
//
//	public void setDateEntryFlag(int dateEntryFlag) {
//		this.dateEntryFlag = dateEntryFlag;
//	}
//
//	public float getRepairCosts() {
//		return repairCosts;
//	}
//
//	public void setRepairCosts(float repairCosts) {
//		this.repairCosts = repairCosts;
//	}
//
//	public float getLostProduction() {
//		return lostProduction;
//	}
//
//	public void setLostProduction(float lostProduction) {
//		this.lostProduction = lostProduction;
//	}
//
//	public int getCalCDowntimeFlag() {
//		return calCDowntimeFlag;
//	}
//
//	public void setCalCDowntimeFlag(int calCDowntimeFlag) {
//		this.calCDowntimeFlag = calCDowntimeFlag;
//	}
//
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
//
//	public String getReason() {
//		return reason;
//	}
//
//	public void setReason(String reason) {
//		this.reason = reason;
//	}
//
//	public int getMessageSendFlag() {
//		return messageSendFlag;
//	}
//
//	public void setMessageSendFlag(int messageSendFlag) {
//		this.messageSendFlag = messageSendFlag;
//	}
//
//	public int getDestinationPerson() {
//		return destinationPerson;
//	}
//
//	public void setDestinationPerson(int destinationPerson) {
//		this.destinationPerson = destinationPerson;
//	}
//
//	public String getRioDowntimeId() {
//		return rioDowntimeId;
//	}
//
//	public void setRioDowntimeId(String rioDowntimeId) {
//		this.rioDowntimeId = rioDowntimeId;
//	}
//
//	public float getLastDayHoursDown() {
//		return lastDayHoursDown;
//	}
//
//	public void setLastDayHoursDown(float lastDayHoursDown) {
//		this.lastDayHoursDown = lastDayHoursDown;
//	}
//
//	public int getDeleteFlag() {
//		return deleteFlag;
//	}
//
//	public void setDeleteFlag(int deleteFlag) {
//		this.deleteFlag = deleteFlag;
//	}
//
//	public int getLastTransmission() {
//		return lastTransmission;
//	}
//
//	public void setLastTransmission(int lastTransmission) {
//		this.lastTransmission = lastTransmission;
//	}
//
//	public int getTransmitFlag() {
//		return transmitFlag;
//	}
//
//	public void setTransmitFlag(int transmitFlag) {
//		this.transmitFlag = transmitFlag;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public int getObjectType() {
//		return objectType;
//	}
//
//	public void setObjectType(int objectType) {
//		this.objectType = objectType;
//	}
 
	public Date getOriginalDateEntered() {
		return originalDateEntered;
	}

	public void setOriginalDateEntered(Date originalDateEntered) {
		this.originalDateEntered = originalDateEntered;
	}

	public int getDurationInHours() {
		return durationInHours;
	}

	public void setDurationInHours(int durationInHours) {
		this.durationInHours = durationInHours;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getChildCode() {
		return childCode;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}

	public String getUwiId() {
		return uwiId;
	}

	public void setUwiId(String uwiId) {
		this.uwiId = uwiId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

//	public Date getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//
//	public Date getStartProductionDate() {
//		return startProductionDate;
//	}
//
//	public void setStartProductionDate(Date startProductionDate) {
//		this.startProductionDate = startProductionDate;
//	}
//
//	public Date getEndProducationDate() {
//		return endProducationDate;
//	}
//
//	public void setEndProducationDate(Date endProducationDate) {
//		this.endProducationDate = endProducationDate;
//	}
//
//	public Date getLastLoadDate() {
//		return lastLoadDate;
//	}
//
//	public void setLastLoadDate(Date lastLoadDate) {
//		this.lastLoadDate = lastLoadDate;
//	}
//
//	public Date getDateTimeStamp() {
//		return dateTimeStamp;
//	}
//
//	public void setDateTimeStamp(Date dateTimeStamp) {
//		this.dateTimeStamp = dateTimeStamp;
//	}
//
//	public Date getbLogicDateStamp() {
//		return bLogicDateStamp;
//	}
//
//	public void setbLogicDateStamp(Date bLogicDateStamp) {
//		this.bLogicDateStamp = bLogicDateStamp;
//	}
//
//	public Date getUserDateStamp() {
//		return userDateStamp;
//	}
//
//	public void setUserDateStamp(Date userDateStamp) {
//		this.userDateStamp = userDateStamp;
//	}

	@Override
	public String toString() {
		return "DowntimeCaptureDto [uwiId=" + uwiId + ", originalDateEntered=" + originalDateEntered
				+ ", durationInHours=" + durationInHours + ", durationInMinutes=" + durationInMinutes + ", parentCode="
				+ parentCode + ", childCode=" + childCode + ", startDate=" + startDate + ", comments=" + comments + "]";
	}

}
