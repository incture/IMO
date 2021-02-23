package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "LOC_TIMEZONE_MASTER")
public class LocationTimeZoneMasterDo implements BaseDo, Serializable {

	@Id
	@Column(name = "LOCATION_CODE", length = 30)
	private String locationCode;
	
	@Column(name = "LOCATION_TEXT", length = 30)
	private String locationText;
	
	@Column(name = "TIME_ZONE", length = 30)
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
		return "LocationTimeZoneMasterDo [locationCode=" + locationCode + ", locationText=" + locationText
				+ ", timeZone=" + timeZone + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
}
