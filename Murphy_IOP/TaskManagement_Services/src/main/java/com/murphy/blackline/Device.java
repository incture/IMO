package com.murphy.blackline;

import java.math.BigInteger;

public class Device {

private BigInteger userId;
	
	
	
	public BigInteger getUserId() {
		return userId;
	}
	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBlacklineId() {
		return blacklineId;
	}
	public void setBlacklineId(String blacklineId) {
		this.blacklineId = blacklineId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	private String name;
	private String blacklineId;
	private Double latitude;
	private Double longitude;
	private Boolean isOnline;
	private String firstName;
	private String lastName;


	@Override
	public String toString() {
		return "Device [userId=" + userId + ", name=" + name + ", blacklineId=" + blacklineId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", isOnline=" + isOnline + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	
	
	

}

}
