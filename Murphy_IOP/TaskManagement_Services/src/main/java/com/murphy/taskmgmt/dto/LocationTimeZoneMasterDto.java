package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class LocationTimeZoneMasterDto extends BaseDto{

	private String locationCode;
	private String locationText;
	private String timeZone;
	
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationText() {
		return locationText;
	}
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	@Override
	public String toString() {
		return "LocationTimeZoneMasterDto [locationCode=" + locationCode + ", locationText=" + locationText
				+ ", timeZone=" + timeZone + "]";
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
