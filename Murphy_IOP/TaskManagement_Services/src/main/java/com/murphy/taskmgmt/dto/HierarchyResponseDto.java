package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class HierarchyResponseDto extends BaseDto {

	private String locationType;
	private List<LocationHierarchyDto> locationHierarchy;

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

	@Override
	public String toString() {
		return "HierarchyResponseDto [locationType=" + locationType + ", locationHierarchy=" + locationHierarchy
				+ ", page=" + page + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
