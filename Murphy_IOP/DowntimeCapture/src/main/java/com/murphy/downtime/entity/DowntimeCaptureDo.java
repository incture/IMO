package com.murphy.downtime.entity;

public class DowntimeCaptureDo {

	private int merrickId;
	private int objectType;
	private String originalDateEntered;
	private String OriginalTimeEntered;
	private float downtimeHours;
	private int upDownFlag;
	private int dateEntryFlag;
	private int downtimeCode;
	private float repairCosts;
	private float lostProduction;
	private int calCDowntimeFlag;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String startProductionDate;
	private String endProductionDate;
	private String comments;
	private String reason;
	private int messageSendFlag;
	private int destinationPerson;
	private String rioDowntimeId;
	private float lastDayHoursDown;
	private int deleteFlag;
	private int lastTransmission;
	private String lastLoadDate;
	private String lastLoadTime;
	private int transmitFlag;
	private String dateTimeStamp;
	private String bLogicDateStamp;
	private String bLogicTimeStamp;
	private String userDateStamp;
	private String userTimeStamp;
	private String userId;

	public int getMerrickId() {
		return merrickId;
	}

	public void setMerrickId(int merrickId) {
		this.merrickId = merrickId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getOriginalDateEntered() {
		return originalDateEntered;
	}

	public int getUpDownFlag() {
		return upDownFlag;
	}

	public void setUpDownFlag(int upDownFlag) {
		this.upDownFlag = upDownFlag;
	}

	public int getDateEntryFlag() {
		return dateEntryFlag;
	}

	public void setDateEntryFlag(int dateEntryFlag) {
		this.dateEntryFlag = dateEntryFlag;
	}

	public int getDowntimeCode() {
		return downtimeCode;
	}

	public void setDowntimeCode(int downtimeCode) {
		this.downtimeCode = downtimeCode;
	}

	public float getRepairCosts() {
		return repairCosts;
	}

	public void setRepairCosts(float repairCosts) {
		this.repairCosts = repairCosts;
	}

	public float getLostProduction() {
		return lostProduction;
	}

	public void setLostProduction(float lostProduction) {
		this.lostProduction = lostProduction;
	}

	public int getCalCDowntimeFlag() {
		return calCDowntimeFlag;
	}

	public void setCalCDowntimeFlag(int calCDowntimeFlag) {
		this.calCDowntimeFlag = calCDowntimeFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setOriginalDateEntered(String originalDateEntered) {
		this.originalDateEntered = originalDateEntered;
	}

	public String getOriginalTimeEntered() {
		return OriginalTimeEntered;
	}

	public void setOriginalTimeEntered(String originalTimeEntered) {
		OriginalTimeEntered = originalTimeEntered;
	}

	public float getDowntimeHours() {
		return downtimeHours;
	}

	public void setDowntimeHours(float downtimeHours) {
		this.downtimeHours = downtimeHours;
	}

	public String getStartProductionDate() {
		return startProductionDate;
	}

	public void setStartProductionDate(String startProductionDate) {
		this.startProductionDate = startProductionDate;
	}

	public String getEndProductionDate() {
		return endProductionDate;
	}

	public void setEndProductionDate(String endProductionDate) {
		this.endProductionDate = endProductionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getMessageSendFlag() {
		return messageSendFlag;
	}

	public void setMessageSendFlag(int messageSendFlag) {
		this.messageSendFlag = messageSendFlag;
	}

	public int getDestinationPerson() {
		return destinationPerson;
	}

	public void setDestinationPerson(int destinationPerson) {
		this.destinationPerson = destinationPerson;
	}

	public String getRioDowntimeId() {
		return rioDowntimeId;
	}

	public void setRioDowntimeId(String rioDowntimeId) {
		this.rioDowntimeId = rioDowntimeId;
	}

	public float getLastDayHoursDown() {
		return lastDayHoursDown;
	}

	public void setLastDayHoursDown(float lastDayHoursDown) {
		this.lastDayHoursDown = lastDayHoursDown;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public int getLastTransmission() {
		return lastTransmission;
	}

	public void setLastTransmission(int lastTransmission) {
		this.lastTransmission = lastTransmission;
	}

	public String getLastLoadDate() {
		return lastLoadDate;
	}

	public void setLastLoadDate(String lastLoadDate) {
		this.lastLoadDate = lastLoadDate;
	}

	public String getLastLoadTime() {
		return lastLoadTime;
	}

	public void setLastLoadTime(String lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}

	public int getTransmitFlag() {
		return transmitFlag;
	}

	public void setTransmitFlag(int transmitFlag) {
		this.transmitFlag = transmitFlag;
	}

	public String getDateTimeStamp() {
		return dateTimeStamp;
	}

	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}

	public String getbLogicDateStamp() {
		return bLogicDateStamp;
	}

	public void setbLogicDateStamp(String bLogicDateStamp) {
		this.bLogicDateStamp = bLogicDateStamp;
	}

	public String getbLogicTimeStamp() {
		return bLogicTimeStamp;
	}

	public void setbLogicTimeStamp(String bLogicTimeStamp) {
		this.bLogicTimeStamp = bLogicTimeStamp;
	}

	public String getUserDateStamp() {
		return userDateStamp;
	}

	public void setUserDateStamp(String userDateStamp) {
		this.userDateStamp = userDateStamp;
	}

	public String getUserTimeStamp() {
		return userTimeStamp;
	}

	public void setUserTimeStamp(String userTimeStamp) {
		this.userTimeStamp = userTimeStamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "DowntimeCaptureDo [merrickId=" + merrickId + ", objectType=" + objectType + ", originalDateEntered=" + originalDateEntered
				+ ", OriginalTimeEntered=" + OriginalTimeEntered + ", downtimeHours=" + downtimeHours + ", upDownFlag=" + upDownFlag
				+ ", dateEntryFlag=" + dateEntryFlag + ", downtimeCode=" + downtimeCode + ", repairCosts=" + repairCosts + ", lostProduction="
				+ lostProduction + ", calCDowntimeFlag=" + calCDowntimeFlag + ", startDate=" + startDate + ", endDate=" + endDate + ", startTime="
				+ startTime + ", endTime=" + endTime + ", startProductionDate=" + startProductionDate + ", endProductionDate=" + endProductionDate
				+ ", comments=" + comments + ", reason=" + reason + ", messageSendFlag=" + messageSendFlag + ", destinationPerson="
				+ destinationPerson + ", rioDowntimeId=" + rioDowntimeId + ", lastDayHoursDown=" + lastDayHoursDown + ", deleteFlag=" + deleteFlag
				+ ", lastTransmission=" + lastTransmission + ", lastLoadDate=" + lastLoadDate + ", lastLoadTime=" + lastLoadTime + ", transmitFlag="
				+ transmitFlag + ", dateTimeStamp=" + dateTimeStamp + ", bLogicDateStamp=" + bLogicDateStamp + ", bLogicTimeStamp=" + bLogicTimeStamp
				+ ", userDateStamp=" + userDateStamp + ", userTimeStamp=" + userTimeStamp + ", userId=" + userId + "]";
	}

}
