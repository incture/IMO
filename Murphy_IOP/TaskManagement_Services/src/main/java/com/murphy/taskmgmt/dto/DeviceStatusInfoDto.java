package com.murphy.taskmgmt.dto;

import java.util.Date;

public class DeviceStatusInfoDto {

	private Integer bearing;
	private String currentStateDuration;
	private String dateTime;
	private String parkedDateTime;
	private String deviceId;
	private boolean isDeviceCommunicating;
	private boolean isDriving;
	private Double latitude;
	private Double longitude;
	private Integer speed;
	private Double crowFlyDistance;
	private Double roadDistance;
	private String muwi;
	private String wellName;
	private String wellPad;
	private String facility;
	private String field;
	private Date parkEndTime;
	private Double wellLat;
	private Double wellLon;

	public String getParkedDateTime() {
		return parkedDateTime;
	}

	public void setParkedDateTime(String parkedDateTime) {
		this.parkedDateTime = parkedDateTime;
	}

	public Integer getBearing() {
		return bearing;
	}

	public void setBearing(Integer bearing) {
		this.bearing = bearing;
	}

	public String getCurrentStateDuration() {
		return currentStateDuration;
	}

	public void setCurrentStateDuration(String currentStateDuration) {
		this.currentStateDuration = currentStateDuration;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isDeviceCommunicating() {
		return isDeviceCommunicating;
	}

	public void setDeviceCommunicating(boolean isDeviceCommunicating) {
		this.isDeviceCommunicating = isDeviceCommunicating;
	}

	public boolean isDriving() {
		return isDriving;
	}

	public void setDriving(boolean isDriving) {
		this.isDriving = isDriving;
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

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
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

	public String getWellPad() {
		return wellPad;
	}

	public void setWellPad(String wellPad) {
		this.wellPad = wellPad;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
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

	@Override
	public String toString() {
		return "DeviceStatusInfoDto [bearing=" + bearing + ", currentStateDuration=" + currentStateDuration
				+ ", dateTime=" + dateTime + ", parkedDateTime=" + parkedDateTime + ", deviceId=" + deviceId
				+ ", isDeviceCommunicating=" + isDeviceCommunicating + ", isDriving=" + isDriving + ", latitude="
				+ latitude + ", longitude=" + longitude + ", speed=" + speed + ", crowFlyDistance=" + crowFlyDistance
				+ ", roadDistance=" + roadDistance + ", muwi=" + muwi + ", wellName=" + wellName + ", wellPad="
				+ wellPad + ", facility=" + facility + ", field=" + field + ", parkEndTime=" + parkEndTime
				+ ", wellLat=" + wellLat + ", wellLon=" + wellLon + "]";
	}

	public Date getParkEndTime() {
		return parkEndTime;
	}

	public void setParkEndTime(Date parkEndTime) {
		this.parkEndTime = parkEndTime;
	}

}
