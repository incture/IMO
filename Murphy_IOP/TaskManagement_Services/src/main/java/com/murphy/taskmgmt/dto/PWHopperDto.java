package com.murphy.taskmgmt.dto;

public class PWHopperDto  {

	private String location;
	private String locationCode;
	private String muwi ;
	private String potentialUplift;
	private String workOverCost;
	private String boe;
	private String potStatus;
	private String reStatus;
	private String wwStatus;
	private String alsStatus;
	private String hopperId;
	private String investigationTaskId;
	private String investigationProcessId;
	private boolean hasInvestigation;
	private boolean isProactive;
	private String locationType;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public String getPotentialUplift() {
		return potentialUplift;
	}
	public void setPotentialUplift(String potentialUplift) {
		this.potentialUplift = potentialUplift;
	}
	public String getWorkOverCost() {
		return workOverCost;
	}
	public void setWorkOverCost(String workOverCost) {
		this.workOverCost = workOverCost;
	}
	public String getBoe() {
		return boe;
	}
	public void setBoe(String boe) {
		this.boe = boe;
	}
	public String getPotStatus() {
		return potStatus;
	}
	public void setPotStatus(String potStatus) {
		this.potStatus = potStatus;
	}
	public String getReStatus() {
		return reStatus;
	}
	public void setReStatus(String reStatus) {
		this.reStatus = reStatus;
	}
	public String getWwStatus() {
		return wwStatus;
	}
	public void setWwStatus(String wwStatus) {
		this.wwStatus = wwStatus;
	}
	public String getAlsStatus() {
		return alsStatus;
	}
	public void setAlsStatus(String alsStatus) {
		this.alsStatus = alsStatus;
	}
	public String getHopperId() {
		return hopperId;
	}
	public void setHopperId(String hopperId) {
		this.hopperId = hopperId;
	}
	public String getInvestigationTaskId() {
		return investigationTaskId;
	}
	public void setInvestigationTaskId(String investigationTaskId) {
		this.investigationTaskId = investigationTaskId;
	}
	public boolean isHasInvestigation() {
		return hasInvestigation;
	}
	public void setHasInvestigation(boolean hasInvestigation) {
		this.hasInvestigation = hasInvestigation;
	}
	public boolean isProactive() {
		return isProactive;
	}
	public void setProactive(boolean isProactive) {
		this.isProactive = isProactive;
	}
	
	@Override
	public String toString() {
		return "PWHopperDto [location=" + location + ", locationCode=" + locationCode + ", muwi=" + muwi
				+ ", potentialUplift=" + potentialUplift + ", workOverCost=" + workOverCost + ", boe=" + boe
				+ ", potStatus=" + potStatus + ", reStatus=" + reStatus + ", wwStatus=" + wwStatus + ", alsStatus="
				+ alsStatus + ", hopperId=" + hopperId + ", investigationId=" + investigationTaskId + ", hasInvestigation="
				+ hasInvestigation + ", isProactive=" + isProactive + "]";
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getInvestigationProcessId() {
		return investigationProcessId;
	}
	public void setInvestigationProcessId(String investigationProcessId) {
		this.investigationProcessId = investigationProcessId;
	}


}


