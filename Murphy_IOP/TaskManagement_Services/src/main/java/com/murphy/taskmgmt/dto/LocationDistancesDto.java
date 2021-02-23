package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class LocationDistancesDto extends BaseDto {
	private String fromLocCode;
	private String toLocCode;
	private Double fromLatitude;
	private Double fromLongitude;
	private Double toLatitude;
	private Double toLongitude;
	private Double roadDriveTime;
	private Double roadTotalTime;
	private Double roadLength;
	private Double crowFlyLength;
	private ResponseMessage responseMessage;

	public LocationDistancesDto() {
		this.roadDriveTime = 0.0;
		this.roadTotalTime = 0.0;
		this.crowFlyLength = 0.0;
	}

	public String getFromLocCode() {
		return fromLocCode;
	}

	public void setFromLocCode(String fromLocCode) {
		this.fromLocCode = fromLocCode;
	}

	public String getToLocCode() {
		return toLocCode;
	}

	public void setToLocCode(String toLocCode) {
		this.toLocCode = toLocCode;
	}

	public Double getFromLatitude() {
		return fromLatitude;
	}

	public void setFromLatitude(Double fromLatitude) {
		this.fromLatitude = fromLatitude;
	}

	public Double getFromLongitude() {
		return fromLongitude;
	}

	public void setFromLongitude(Double fromLongitude) {
		this.fromLongitude = fromLongitude;
	}

	public Double getToLatitude() {
		return toLatitude;
	}

	public void setToLatitude(Double toLatitude) {
		this.toLatitude = toLatitude;
	}

	public Double getToLongitude() {
		return toLongitude;
	}

	public void setToLongitude(Double toLongitude) {
		this.toLongitude = toLongitude;
	}

	public Double getRoadDriveTime() {
		return roadDriveTime;
	}

	public void setRoadDriveTime(Double roadDriveTime) {
		this.roadDriveTime = roadDriveTime;
	}

	public Double getRoadTotalTime() {
		return roadTotalTime;
	}

	public void setRoadTotalTime(Double roadTotalTime) {
		this.roadTotalTime = roadTotalTime;
	}

	public Double getRoadLength() {
		return roadLength;
	}

	public void setRoadLength(Double roadLength) {
		this.roadLength = roadLength;
	}

	public Double getCrowFlyLength() {
		return crowFlyLength;
	}

	public void setCrowFlyLength(Double crowFlyLength) {
		this.crowFlyLength = crowFlyLength;
	}
	
	

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}


	@Override
	public String toString() {
		return "LocationDistancesDto [fromLocCode=" + fromLocCode + ", toLocCode=" + toLocCode + ", fromLatitude="
				+ fromLatitude + ", fromLongitude=" + fromLongitude + ", toLatitude=" + toLatitude + ", toLongitude="
				+ toLongitude + ", roadDriveTime=" + roadDriveTime + ", roadTotalTime=" + roadTotalTime
				+ ", roadLength=" + roadLength + ", crowFlyLength=" + crowFlyLength + ", responseMessage="
				+ responseMessage + "]";
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
