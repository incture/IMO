package com.murphy.taskmgmt.dto;

import java.util.ArrayList;

public class WellVisitDayMatrixDto {

	private ArrayList<String> locationCodes;
	private Double workLoad;

	public ArrayList<String> getLocationCodes() {
		return locationCodes;
	}

	public void setLocationCodes(ArrayList<String> locationCodes) {
		this.locationCodes = locationCodes;
	}

	public Double getWorkLoad() {
		return workLoad;
	}

	public void setWorkLoad(Double workLoad) {
		this.workLoad = workLoad;
	}

	@Override
	public String toString() {
		return "WellVisitDayMatrixDto [locationCodes=" + locationCodes + ", workLoad=" + workLoad + "]";
	}

}
