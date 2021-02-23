package com.murphy.taskmgmt.dto;

import java.util.Date;

public class FracPackEngViewDto {

	private long fracId;
	
	private String funcLocation;
	
	private String wellName;
	
	private Date fracDate;
	
	private String wellCode;
	
	private Date estBolDate;
	
	private Double prodImpact;

	private Double maxTubePressure;

	private Double maxCasePressure;
	
	private Double activeTubePressure;
	
	private Double activeCasePressure;
	
	private Double maxPsi;
	
	private Double distFrac;
	
	private String FracSla;
	
	private String fracHit;
	
	private Date timeStamp;
	
	private String wellStatus;
	
	private String color;
	
	private String locationCode;
	
	private Double boed;
	
	//Adding for incident INC0078316
	private String scenario;
	
	//Adding for CHG0037278
	private String orientation;

	private String zone;
	
	private String fracStatus;
	
	private Date startAt;
	
	private Date endAt;
	
	private String description;
	
	private String userId;
	
	private String userRole;
	
	//end CHG0037278

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public Double getBoed() {
		return boed;
	}

	public void setBoed(Double boed) {
		this.boed = boed;
	}

	public long getFracId() {
		return fracId;
	}

	public void setFracId(long fracId) {
		this.fracId = fracId;
	}

	public String getFuncLocation() {
		return funcLocation;
	}

	public void setFuncLocation(String funcLocation) {
		this.funcLocation = funcLocation;
	}

	public String getWellName() {
		return wellName;
	}
	

	public String getWellCode() {
		return wellCode;
	}

	public void setWellCode(String wellCode) {
		this.wellCode = wellCode;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public Date getFracDate() {
		return fracDate;
	}

	public void setFracDate(Date fracDate) {
		this.fracDate = fracDate;
	}

	public Date getEstBolDate() {
		return estBolDate;
	}

	public void setEstBolDate(Date estBolDate) {
		this.estBolDate = estBolDate;
	}

	public Double getProdImpact() {
		return prodImpact;
	}

	public void setProdImpact(Double prodImpact) {
		this.prodImpact = prodImpact;
	}

	public Double getMaxTubePressure() {
		return maxTubePressure;
	}

	public void setMaxTubePressure(Double maxTubePressure) {
		this.maxTubePressure = maxTubePressure;
	}

	public Double getMaxCasePressure() {
		return maxCasePressure;
	}

	public void setMaxCasePressure(Double maxCasePressure) {
		this.maxCasePressure = maxCasePressure;
	}

	public Double getActiveTubePressure() {
		return activeTubePressure;
	}

	public void setActiveTubePressure(Double activeTubePressure) {
		this.activeTubePressure = activeTubePressure;
	}

	public Double getActiveCasePressure() {
		return activeCasePressure;
	}

	public void setActiveCasePressure(Double activeCasePressure) {
		this.activeCasePressure = activeCasePressure;
	}

	public Double getMaxPsi() {
		return maxPsi;
	}

	public void setMaxPsi(Double maxPsi) {
		this.maxPsi = maxPsi;
	}

	public Double getDistFrac() {
		return distFrac;
	}

	public void setDistFrac(Double distFrac) {
		this.distFrac = distFrac;
	}

	public String getFracSla() {
		return FracSla;
	}

	public void setFracSla(String fracSla) {
		FracSla = fracSla;
	}

	public String getFracHit() {
		return fracHit;
	}

	public void setFracHit(String fracHit) {
		this.fracHit = fracHit;
	}

	

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getFracStatus() {
		return fracStatus;
	}

	public void setFracStatus(String fracStatus) {
		this.fracStatus = fracStatus;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
