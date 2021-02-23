package com.murphy.taskmgmt.dto;

public class IsolationDetailsDto {

	private String isolationDetail;
	private String location;
	private String isEnergyStored;
	private String isEnergyNotStored;
	private String isEquipmentTested;
	private String isEquipmentNotTested;
	private String isolationDate;
	private String isolationTime;
	private String reInstateMentDate;
	private String reInstateMentTime;
	
	public String getIsolationDetail() {
		return isolationDetail;
	}
	public void setIsolationDetail(String isolationDetail) {
		this.isolationDetail = isolationDetail;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIsEnergyStored() {
		return isEnergyStored;
	}
	public void setIsEnergyStored(String isEnergyStored) {
		this.isEnergyStored = isEnergyStored;
	}
	public String getIsEquipmentTested() {
		return isEquipmentTested;
	}
	public void setIsEquipmentTested(String isEquipmentTested) {
		this.isEquipmentTested = isEquipmentTested;
	}
	public String getIsolationDate() {
		return isolationDate;
	}
	public void setIsolationDate(String isolationDate) {
		this.isolationDate = isolationDate;
	}
	public String getIsolationTime() {
		return isolationTime;
	}
	public void setIsolationTime(String isolationTime) {
		this.isolationTime = isolationTime;
	}
	public String getReInstateMentDate() {
		return reInstateMentDate;
	}
	public void setReInstateMentDate(String reInstateMentDate) {
		this.reInstateMentDate = reInstateMentDate;
	}
	public String getReInstateMentTime() {
		return reInstateMentTime;
	}
	public void setReInstateMentTime(String reInstateMentTime) {
		this.reInstateMentTime = reInstateMentTime;
	}
	
	public String getIsEnergyNotStored() {
		return isEnergyNotStored;
	}
	public void setIsEnergyNotStored(String isEnergyNotStored) {
		this.isEnergyNotStored = isEnergyNotStored;
	}
	public String getIsEquipmentNotTested() {
		return isEquipmentNotTested;
	}
	public void setIsEquipmentNotTested(String isEquipmentNotTested) {
		this.isEquipmentNotTested = isEquipmentNotTested;
	}
	
	@Override
	public String toString() {
		return "IsolationDetailsDto [isolationDetail=" + isolationDetail + ", location=" + location
				+ ", isEnergyStored=" + isEnergyStored + ", isEnergyNotStored=" + isEnergyNotStored
				+ ", isEquipmentTested=" + isEquipmentTested + ", isEquipmentNotTested=" + isEquipmentNotTested
				+ ", isolationDate=" + isolationDate + ", isolationTime=" + isolationTime + ", reInstateMentDate="
				+ reInstateMentDate + ", reInstateMentTime=" + reInstateMentTime + "]";
	}

	
	
	
	
	
}
