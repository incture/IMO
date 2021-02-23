package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RouteLocationDoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1447149411406980120L;

	
	@Column(name = "EMP_ID", length = 100)
	private String empId;

	@Column(name = "LOCATION", length = 100)
	private String location;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "RouteLocationDoPK [empId=" + empId + ", location=" + location + "]";
	}
	
	
}
