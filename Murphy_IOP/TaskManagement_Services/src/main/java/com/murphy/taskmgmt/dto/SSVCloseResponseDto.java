package com.murphy.taskmgmt.dto;

import java.util.Date;

public class SSVCloseResponseDto {

	private String wellName;
	private String parentCode;
	private String childCode;
	private Date startDate;
	private Date endDate;
	private int downTimeInHours;
	private int downTimeMinutes;
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getDownTimeInHours() {
		return downTimeInHours;
	}
	public void setDownTimeInHours(int downTimeInHours) {
		this.downTimeInHours = downTimeInHours;
	}
	public int getDownTimeMinutes() {
		return downTimeMinutes;
	}
	public void setDownTimeMinutes(int downTimeMinutes) {
		this.downTimeMinutes = downTimeMinutes;
	}
	@Override
	public String toString() {
		return "SSVCloseResponseDto [wellName=" + wellName + ", parentCode=" + parentCode + ", childCode=" + childCode
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", downTimeInHours=" + downTimeInHours
				+ ", downTimeMinutes=" + downTimeMinutes + "]";
	}
	
	
	
	
	
	
}
