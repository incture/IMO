package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeRequestDto extends BaseDto {

	private String locationType;
	private List<LocationHierarchyDto> locationHierarchy;
	private String statusType;
	// Attribute for location History
	private String monthTime;
	private int page_size;
	private String type_selected;
	private String countryCode;
	private String userTimeZone;

	public String getType_selected() {
		return type_selected;
	}

	public void setType_selected(String type_selected) {
		this.type_selected = type_selected;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	/* Attributes for Pagination in Alarm Data */
	private int page;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public List<LocationHierarchyDto> getLocationHierarchy() {
		return locationHierarchy;
	}

	public void setLocationHierarchy(List<LocationHierarchyDto> locationHierarchy) {
		this.locationHierarchy = locationHierarchy;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getUserTimeZone() {
		return userTimeZone;
	}

	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	@Override
	public String toString() {
		return "DowntimeRequestDto [locationType=" + locationType + ", locationHierarchy=" + locationHierarchy
				+ ", statusType=" + statusType + ", monthTime=" + monthTime + ", page_size=" + page_size
				+ ", type_selected=" + type_selected + ", countryCode=" + countryCode + ", userTimeZone=" + userTimeZone
				+ ", page=" + page + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

	public String getMonthTime() {
		return monthTime;
	}

	public void setMonthTime(String monthTime) {
		this.monthTime = monthTime;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

}
