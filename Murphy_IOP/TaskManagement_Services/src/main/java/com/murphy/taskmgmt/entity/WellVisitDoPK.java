package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;

public class WellVisitDoPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2331387189573089061L;

	@Column(name="DAY")
	private int day;
	
	@Column(name="LOCATION_CODE", length=100)
	private String locationCode;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	
	
}
