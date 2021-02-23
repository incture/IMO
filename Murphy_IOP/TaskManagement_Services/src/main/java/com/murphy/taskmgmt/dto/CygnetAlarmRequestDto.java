package com.murphy.taskmgmt.dto;

public class CygnetAlarmRequestDto {

	/* Attributes for selection in Alarm Data */
	private String locationType;
	private String locations;

	/* Attributes for Divisions in Alarm Data */
	private boolean acknowledged;
	private boolean well;

	/* Attributes for Pagination in Alarm Data */
	private int page;

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public boolean Acknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public boolean Well() {
		return well;
	}

	public void setWell(boolean well) {
		this.well = well;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "CygnetAlarmRequestDto [locationType=" + locationType + ", locations=" + locations + ", acknowledged="
				+ acknowledged + ", well=" + well + ", page=" + page + "]";
	}

}
