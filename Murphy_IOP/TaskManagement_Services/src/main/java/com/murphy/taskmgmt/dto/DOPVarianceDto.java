package com.murphy.taskmgmt.dto;

public class DOPVarianceDto {

	private String location;
	private String tier;
	private String muwi;
	private String locationCode;
	private String locationType;
	private double actualBoed;
	private double forecastBoed;
	private double projectedBoed;
	private double variance;
	private double variancePercent;
	private boolean hasInvestigation;
	private boolean isDispatch;
	private  boolean isAcknowledge;
	private String dispatchTaskId;
	private String investigationTaskId;
	private String investigationProcessId;
	private String alarmSeverity;
	private double maxValue;
	private String inquiryTaskId;
	private boolean hasInquiry;
	private boolean isProActive;
	

	public double getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getTier() {
		return tier;
	}


	public void setTier(String tier) {
		this.tier = tier;
	}


	public String getMuwi() {
		return muwi;
	}


	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}


	public String getLocationCode() {
		return locationCode;
	}


	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}


	public String getLocationType() {
		return locationType;
	}


	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}


	public double getActualBoed() {
		return actualBoed;
	}


	public void setActualBoed(double actualBoed) {
		this.actualBoed = actualBoed;
	}


	public double getVariance() {
		return variance;
	}


	public void setVariance(double variance) {
		this.variance = variance;
	}


	public double getVariancePercent() {
		return variancePercent;
	}


	public void setVariancePercent(double variancePercent) {
		this.variancePercent = variancePercent;
	}


	public boolean isHasInvestigation() {
		return hasInvestigation;
	}


	public void setHasInvestigation(boolean hasInvestigation) {
		this.hasInvestigation = hasInvestigation;
	}


	public boolean isDispatch() {
		return isDispatch;
	}


	public void setDispatch(boolean isDispatch) {
		this.isDispatch = isDispatch;
	}


	public boolean isAcknowledge() {
		return isAcknowledge;
	}


	public void setAcknowledge(boolean isAcknowledge) {
		this.isAcknowledge = isAcknowledge;
	}


	public String getDispatchTaskId() {
		return dispatchTaskId;
	}


	public void setDispatchTaskId(String dispatchTaskId) {
		this.dispatchTaskId = dispatchTaskId;
	}


	public String getInvestigationTaskId() {
		return investigationTaskId;
	}


	public void setInvestigationTaskId(String investigationTaskId) {
		this.investigationTaskId = investigationTaskId;
	}


	public String getAlarmSeverity() {
		return alarmSeverity;
	}


	public void setAlarmSeverity(String alarmSeverity) {
		this.alarmSeverity = alarmSeverity;
	}


	public String getInvestigationProcessId() {
		return investigationProcessId;
	}


	public void setInvestigationProcessId(String investigationProcessId) {
		this.investigationProcessId = investigationProcessId;
	}


	public double getProjectedBoed() {
		return projectedBoed;
	}


	public void setProjectedBoed(double projectedBoed) {
		this.projectedBoed = projectedBoed;
	}


	public double getForecastBoed() {
		return forecastBoed;
	}


	public void setForecastBoed(double forecastBoed) {
		this.forecastBoed = forecastBoed;
	}


	public boolean isHasInquiry() {
		return hasInquiry;
	}


	public void setHasInquiry(boolean hasInquiry) {
		this.hasInquiry = hasInquiry;
	}


	public String getInquiryTaskId() {
		return inquiryTaskId;
	}


	public void setInquiryTaskId(String inquiryTaskId) {
		this.inquiryTaskId = inquiryTaskId;
	}


	@Override
	public String toString() {
		return "DOPVarianceDto [location=" + location + ", tier=" + tier + ", muwi=" + muwi + ", locationCode="
				+ locationCode + ", locationType=" + locationType + ", actualBoed=" + actualBoed + ", forecastBoed="
				+ forecastBoed + ", projectedBoed=" + projectedBoed + ", variance=" + variance + ", variancePercent="
				+ variancePercent + ", hasInvestigation=" + hasInvestigation + ", isDispatch=" + isDispatch
				+ ", isAcknowledge=" + isAcknowledge + ", dispatchTaskId=" + dispatchTaskId + ", investigationTaskId="
				+ investigationTaskId + ", investigationProcessId=" + investigationProcessId + ", alarmSeverity="
				+ alarmSeverity + ", maxValue=" + maxValue + ", inquiryTaskId=" + inquiryTaskId + ", hasInquiry="
				+ hasInquiry + "]";
	}


	public boolean isProActive() {
		return isProActive;
	}


	public void setProActive(boolean isProActive) {
		this.isProActive = isProActive;
	}
	
	
}
