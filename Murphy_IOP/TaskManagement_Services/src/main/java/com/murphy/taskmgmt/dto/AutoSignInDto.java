package com.murphy.taskmgmt.dto;

import java.util.Date;

public class AutoSignInDto {

//	private String signInId;
	private String driverId;
//	private String driverName;
	private Double driverLat;
	private Double driverLon;
	private String muwi;
	private String wellName;
	private Double wellLat;
	private Double wellLon;
	private Date signInStart;
	private Date signInEnd = null;
	private String timeSignedIn;
	private Double crowFlyDistance;
	private Double roadDistance;
//	private Boolean driverInField;

//	public String getSignInId() {
//		return signInId;
//	}
//
//	public void setSignInId(String signInId) {
//		this.signInId = signInId;
//	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

//	public String getDriverName() {
//		return driverName;
//	}
//
//	public void setDriverName(String driverName) {
//		this.driverName = driverName;
//	}

	public Double getDriverLat() {
		return driverLat;
	}

	public void setDriverLat(Double driverLat) {
		this.driverLat = driverLat;
	}

	public Double getDriverLon() {
		return driverLon;
	}

	public void setDriverLon(Double driverLon) {
		this.driverLon = driverLon;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public Double getWellLat() {
		return wellLat;
	}

	public void setWellLat(Double wellLat) {
		this.wellLat = wellLat;
	}

	public Double getWellLon() {
		return wellLon;
	}

	public void setWellLon(Double wellLon) {
		this.wellLon = wellLon;
	}

	public Date getSignInStart() {
		return signInStart;
	}

	public void setSignInStart(Date signInStart) {
		this.signInStart = signInStart;
	}

	public Date getSignInEnd() {
		return signInEnd;
	}

	public void setSignInEnd(Date signInEnd) {
		this.signInEnd = signInEnd;
	}

	public String getTimeSignedIn() {
		return timeSignedIn;
	}

	public void setTimeSignedIn(String timeSignedIn) {
		this.timeSignedIn = timeSignedIn;
	}

	public Double getCrowFlyDistance() {
		return crowFlyDistance;
	}

	public void setCrowFlyDistance(Double crowFlyDistance) {
		this.crowFlyDistance = crowFlyDistance;
	}

	public Double getRoadDistance() {
		return roadDistance;
	}

	public void setRoadDistance(Double roadDistance) {
		this.roadDistance = roadDistance;
	}

//	public Boolean getDriverInField() {
//		return driverInField;
//	}
//
//	public void setDriverInField(Boolean driverInField) {
//		this.driverInField = driverInField;
//	}

	@Override
	public String toString() {
		return "AutoSignInDto [driverId=" + driverId + ", driverLat=" + driverLat + ", driverLon=" + driverLon
				+ ", muwi=" + muwi + ", wellName=" + wellName + ", wellLat=" + wellLat + ", wellLon=" + wellLon
				+ ", signInStart=" + signInStart + ", signInEnd=" + signInEnd + ", timeSignedIn=" + timeSignedIn
				+ ", crowFlyDistance=" + crowFlyDistance + ", roadDistance=" + roadDistance + "]";
	}

}
