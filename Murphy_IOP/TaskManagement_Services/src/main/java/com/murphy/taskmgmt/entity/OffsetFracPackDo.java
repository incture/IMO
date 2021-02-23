package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="OFFSET_FRAC_PACK")
public class OffsetFracPackDo implements BaseDo,Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="FRAC_ID", length=32)
	private long fracId;
	

	@Id
	@Column(name="FIELD_CODE", length=80)
	private String fieldCode;
	
	@Id
	@Column(name="WELL_CODE", length=60)
	private String wellCode;
	
	@Column(name="WELL_NAME", length=60)
	private String wellName;
	
	@Column(name="WELL_STATUS", length=100)
	private String wellStatus;
	
	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startAt;
	
	@Column(name="END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endAt;
	
	@Column(name="EST_BOL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date estBolDate;
	
	@Column(name="SCENARIO", length=100)
	private String scenario;
	
	@Column(name="STATUS",length=50)
	private String fracStatus;
	
	@Column(name="PROD_IMPACT")
	private Double prodImpact;
	
	@Column(name="MAX_TUBE_PRESSURE")
	private Double maxTubePressure;
	
	@Column(name="MAX_CASE_PRESSURE")
	private Double maxCasePressure;
	
	@Column(name="BOED")
	private Double boed;
	
	@Column(name="DISTANCE_FROM_FRAC")
	private Double distFrac;
	
	@Column(name="ORIENTATION",length=30)
	private String orientation;
	
	@Column(name="ZONE", length=30)
	private String zone;
	
	@Column(name="DESCRIPTION", length=255)
	private String description;

	@Column(name="USER_ID", length=60)
	private String userId;
	
	@Column(name="USER_ROLE", length=60)
	private String userRole;
	
	// Adding for CHG0036988 - to hold Maximum of CASE PSI Reached
	@Column(name = "MAX_CASE_PSI")
	private Double maxCasePSI;
	
	@Column(name = "ACT_TUBE_PRESSURE")
	private Double activeTubePressure;
	
	
	@Column(name = "ACT_CASE_PRESSURE")
	private Double activeCasePressure;
		
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

	
	
	public String getFracStatus() {
		return fracStatus;
	}

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}

	public void setFracStatus(String fracStatus) {
		this.fracStatus = fracStatus;
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
	public String toString() {
		return "OffsetFracPackDo [fracId=" + fracId + ", fieldCode=" + fieldCode + ", wellCode=" + wellCode
				+ ", wellName=" + wellName + ", wellStatus=" + wellStatus + ", startAt=" + startAt + ", endAt=" + endAt
				+ ", estBolDate=" + estBolDate + ", scenario=" + scenario + ", fracStatus=" + fracStatus
				+ ", prodImpact=" + prodImpact + ", maxTubePressure=" + maxTubePressure + ", maxCasePressure="
				+ maxCasePressure + ", boed=" + boed + ", distFrac=" + distFrac + ", orientation=" + orientation
				+ ", zone=" + zone + ", description=" + description + ", userId=" + userId + ", userRole=" + userRole
				+ ", maxCasePSI=" + maxCasePSI + ", activeTubePressure=" + activeTubePressure + ", activeCasePressure="
				+ activeCasePressure + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
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
	
}
