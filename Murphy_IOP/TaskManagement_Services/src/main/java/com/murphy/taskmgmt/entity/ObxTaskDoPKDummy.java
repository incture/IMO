package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;

public class ObxTaskDoPKDummy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6002760241783383251L;
	@Column(name="LOCATION_CODE",length=80)
	private String locationCode;
	
	@Column(name="DAY")
	private int day;

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "ObxTaskDoPK [locationCode=" + locationCode + ", day=" + day + "]";
	}
	
}
