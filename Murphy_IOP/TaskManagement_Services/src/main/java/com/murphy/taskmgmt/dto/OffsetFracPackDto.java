package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;



public class OffsetFracPackDto extends BaseDto{


	
	private long fracId;

	private String fieldCode;
	
	private String wellCode;
	
	private String wellName;
	
	private Date startAt;

	private Date endAt;
	 
	private String fracStatus;
	
	private Date estBolDate;
	
	private String scenario;
	
	private Double prodImpact;
	
	private Double maxTubePressure;
	
	private Double maxCasePressure;
	
	//Adding for CHG0036988 to hold maximum of CASE PSI Reached
	private Double maxCasePSI;
	
	private Double boed;
	
	private Double distFrac;

	private String orientation;

	private String zone;
	
	private String description;
	

	
	private String location;
	
	private String userId;
	
	private String userRole;
	
	private String wellStatus;
	
	private String action;
	
	private Double activeTubePressure;
	
	private Double activeCasePressure;
	
	

	public String getAction() {
		return action;
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

	public void setAction(String action) {
		this.action = action;
	}

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}

	public String getFracStatus() {
		return fracStatus;
	}

	public void setFracStatus(String fracStatus) {
		this.fracStatus = fracStatus;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
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

	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getFracId() {
		return fracId;
	}

	public void setFracId(long fracId) {
		this.fracId = fracId;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getWellCode() {
		return wellCode;
	}

	public void setWellCode(String wellCode) {
		this.wellCode = wellCode;
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

	public Date getEstBolDate() {
		return estBolDate;
	}

	public void setEstBolDate(Date estBolDate) {
		this.estBolDate = estBolDate;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
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


	public Double getMaxCasePSI() {
		return maxCasePSI;
	}

	public void setMaxCasePSI(Double maxCasePSI) {
		this.maxCasePSI = maxCasePSI;
	}

	public Double getBoed() {
		return boed;
	}

	public void setBoed(Double boed) {
		this.boed = boed;
	}

	public Double getDistFrac() {
		return distFrac;
	}

	public void setDistFrac(Double distFrac) {
		this.distFrac = distFrac;
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

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
}
