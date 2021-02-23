package com.murphy.taskmgmt.dto;

public class ArcGISBestSequesnceDto {

	private int locationId;
	private int sequenceNumber;
	private Double totalDriveTime;

	public ArcGISBestSequesnceDto(int locationId, int sequenceNumber, Double totalDriveTime) {
		this.locationId = locationId;
		this.sequenceNumber = sequenceNumber;
		this.totalDriveTime = totalDriveTime;
	}

	public Double getTotalDriveTime() {
		return totalDriveTime;
	}

	public void setTotalDriveTime(Double totalDriveTime) {
		this.totalDriveTime = totalDriveTime;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	@Override
	public String toString() {
		return "ArcGISBestSequesnceDto [locationId=" + locationId + ", sequenceNumber=" + sequenceNumber
				+ ", totalDriveTime=" + totalDriveTime + "]";
	}

}
