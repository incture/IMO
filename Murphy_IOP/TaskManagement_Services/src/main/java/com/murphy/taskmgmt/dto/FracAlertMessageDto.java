package com.murphy.taskmgmt.dto;

public class FracAlertMessageDto {

	private String fracAlertMessage;
	private String muwi;
	private String fracId;
	private double maxTubePressure;
	private double maxCasePressure;
	private double activeTubePressure;
	private double activeCasePressure;
	private String wellName;
	private String locationCode;
	private String locationType;
	
	
	
	public String getFracAlertMessage() {
		return fracAlertMessage;
	}
	public void setFracAlertMessage(String fracAlertMessage) {
		this.fracAlertMessage = fracAlertMessage;
	}
	public String getFracId() {
		return fracId;
	}
	public void setFracId(String fracId) {
		this.fracId = fracId;
	}
	
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public double getMaxTubePressure() {
		return maxTubePressure;
	}
	public void setMaxTubePressure(double maxTubePressure) {
		this.maxTubePressure = maxTubePressure;
	}
	public double getMaxCasePressure() {
		return maxCasePressure;
	}
	public void setMaxCasePressure(double maxCasePressure) {
		this.maxCasePressure = maxCasePressure;
	}
	public double getActiveTubePressure() {
		return activeTubePressure;
	}
	public void setActiveTubePressure(double activeTubePressure) {
		this.activeTubePressure = activeTubePressure;
	}
	public double getActiveCasePressure() {
		return activeCasePressure;
	}
	public void setActiveCasePressure(double activeCasePressure) {
		this.activeCasePressure = activeCasePressure;
	}
	
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
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
	@Override
	public String toString() {
		return "FracAlertMessageDto [fracAlertMessage=" + fracAlertMessage + ", muwi=" + muwi + ", fracId=" + fracId
				+ ", maxTubePressure=" + maxTubePressure + ", maxCasePressure=" + maxCasePressure
				+ ", activeTubePressure=" + activeTubePressure + ", activeCasePressure=" + activeCasePressure
				+ ", wellName=" + wellName + ", locationCode=" + locationCode + ", locationType=" + locationType + "]";
	}
	
	
	
	
	
	
}
