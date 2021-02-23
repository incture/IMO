package com.murphy.taskmgmt.dto;

/**
 * @author Rashmendra.Sai
 *
 */

public class ATSTaskListDto {

	private String taskId;
	private String processId;
	private String origin;
	private String locationCode;
	private String muwiID;
	private String classification;
	private String subClassification;
	private String locText;
	private String facilityLocCode;
	private Double wellLatCoord;
	private Double wellLongCoord;
	private Long riskBBL;
	private Double facilityLatCoord;
	private Double facilityLongCoord;
	private String faciltyText;
	private Double distanceFromFacility;
	private Double roadDriveTimeFromFixedCF;
	private String koneWestFacilityCode;
	private String fieldName;
	
	@Override
	public String toString() {
		return "ATSTaskListDto [taskId=" + taskId + ", processId=" + processId + ", origin=" + origin
				+ ", locationCode=" + locationCode + ", muwiID=" + muwiID + ", classification=" + classification
				+ ", subClassification=" + subClassification + ", locText=" + locText + ", facilityLocCode="
				+ facilityLocCode + ", wellLatCoord=" + wellLatCoord + ", wellLongCoord=" + wellLongCoord + ", riskBBL="
				+ riskBBL + ", facilityLatCoord=" + facilityLatCoord + ", facilityLongCoord=" + facilityLongCoord
				+ ", faciltyText=" + faciltyText + ", distanceFromFacility=" + distanceFromFacility
				+ ", roadDriveTimeFromFixedCF=" + roadDriveTimeFromFixedCF + ", koneWestFacilityCode="
				+ koneWestFacilityCode + ", fieldName=" + fieldName + "]";
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getMuwiID() {
		return muwiID;
	}
	public void setMuwiID(String muwiID) {
		this.muwiID = muwiID;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public String getLocText() {
		return locText;
	}

	public void setLocText(String locText) {
		this.locText = locText;
	}

	public String getFacilityLocCode() {
		return facilityLocCode;
	}

	public void setFacilityLocCode(String facilityLocCode) {
		this.facilityLocCode = facilityLocCode;
	}

	public Double getFacilityLatCoord() {
		return facilityLatCoord;
	}

	public void setFacilityLatCoord(Double facilityLatCoord) {
		this.facilityLatCoord = facilityLatCoord;
	}

	public Double getFacilityLongCoord() {
		return facilityLongCoord;
	}

	public void setFacilityLongCoord(Double facilityLongCoord) {
		this.facilityLongCoord = facilityLongCoord;
	}

	public Double getWellLatCoord() {
		return wellLatCoord;
	}

	public Double getWellLongCoord() {
		return wellLongCoord;
	}

	public Long getRiskBBL() {
		return riskBBL;
	}

	public void setRiskBBL(Long riskBBL) {
		this.riskBBL = riskBBL;
	}
	
	public void setWellLatCoord(Double wellLatCoord) {
		this.wellLatCoord = wellLatCoord;
	}

	public void setWellLongCoord(Double wellLongCoord) {
		this.wellLongCoord = wellLongCoord;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getKoneWestFacilityCode() {
		return koneWestFacilityCode;
	}

	public void setKoneWestFacilityCode(String koneWestFacilityCode) {
		this.koneWestFacilityCode = koneWestFacilityCode;
	}

	public Double getDistanceFromFacility() {
		return distanceFromFacility;
	}

	public void setDistanceFromFacility(Double distanceFromFacility) {
		this.distanceFromFacility = distanceFromFacility;
	}

	public String getFaciltyText() {
		return faciltyText;
	}

	public void setFaciltyText(String faciltyText) {
		this.faciltyText = faciltyText;
	}

	public Double getRoadDriveTimeFromFixedCF() {
		return roadDriveTimeFromFixedCF;
	}

	public void setRoadDriveTimeFromFixedCF(Double roadDriveTimeFromFixedCF) {
		this.roadDriveTimeFromFixedCF = roadDriveTimeFromFixedCF;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((muwiID == null) ? 0 : muwiID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ATSTaskListDto other = (ATSTaskListDto) obj;
		if (muwiID == null) {
			if (other.muwiID != null)
				return false;
		} else if (!muwiID.equals(other.muwiID))
			return false;
		return true;
	}
	
	

}
