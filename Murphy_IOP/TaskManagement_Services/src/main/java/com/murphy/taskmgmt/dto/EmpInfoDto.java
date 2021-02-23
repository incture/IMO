package com.murphy.taskmgmt.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class EmpInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String empEmail;
	private String facility;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String locationCode;
	private Integer clusterNo;
	
	
	public Integer getClusterNo() {
		return clusterNo;
	}

	public void setClusterNo(Integer clusterNo) {
		this.clusterNo = clusterNo;
	}

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Override
	public String toString() {
		return "EmpInfoDto [empEmail=" + empEmail + ", facility=" + facility + ", latitude=" + latitude + ", longitude="
				+ longitude + ", locationCode=" + locationCode + ", clusterNo=" + clusterNo + "]";
	}
	
	

}
