package com.murphy.taskmgmt.dto;

import java.util.Date;

public class StartTimeResponseDto {

	@Override
	public String toString() {
		return "StartTimeResponseDto [startTime=" + startTime + ", startTimeInString=" + startTimeInString
				+ ", driveTime=" + driveTime + ", responseMessage=" + responseMessage + "]";
	}
	private Date startTime;
	private String startTimeInString;
	private Double driveTime;
	private ResponseMessage responseMessage;


	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getStartTimeInString() {
		return startTimeInString;
	}
	public void setStartTimeInString(String startTimeInString) {
		this.startTimeInString = startTimeInString;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public Double getDriveTime() {
		return driveTime;
	}
	public void setDriveTime(Double driveTime) {
		this.driveTime = driveTime;
	}


}
