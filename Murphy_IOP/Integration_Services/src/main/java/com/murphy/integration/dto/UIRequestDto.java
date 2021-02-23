package com.murphy.integration.dto;

import java.util.List;

public class UIRequestDto {

	// EnersightProveMonthly
	private String locationType;
	private List<String> locationCodeList;
	private Integer duration;
	private String userType;
	private boolean isRolledUp;
	
	/* Attributes for Pagination in PROVE Data */
	private String page;
	private String countryCode;
	private String reportId;
	
	/*Added for e3m3 prove data */
	private String uom;
	
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public List<String> getLocationCodeList() {
		return locationCodeList;
	}

	public void setLocationCodeList(List<String> locationCodeList) {
		this.locationCodeList = locationCodeList;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UIRequestDto [locationType=" + locationType + ", locationCodeList=" + locationCodeList + ", duration="
				+ duration + ", userType=" + userType + "]";
	}

	public boolean isRolledUp() {
		return isRolledUp;
	}

	public void setRolledUp(boolean isRolledUp) {
		this.isRolledUp = isRolledUp;
	}


}
