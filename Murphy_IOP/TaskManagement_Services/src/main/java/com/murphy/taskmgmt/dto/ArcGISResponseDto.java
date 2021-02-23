package com.murphy.taskmgmt.dto;

public class ArcGISResponseDto {
	
	public ArcGISResponseDto(Double length, Double totalTime, Double totalDriveTime) {
		this.totalLength = length;
		this.totalDriveTime = totalDriveTime;
		this.totalTime = totalTime;
	}
	
	public ArcGISResponseDto() {
		
	}

	private Double totalLength;
	private Double totalTime;
	private Double totalDriveTime;
	
	private ResponseMessage responseMessage;

	public Double getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(Double totalLength) {
		this.totalLength = totalLength;
	}

	public Double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}

	public Double getTotalDriveTime() {
		return totalDriveTime;
	}

	public void setTotalDriveTime(Double totalDriveTime) {
		this.totalDriveTime = totalDriveTime;
	}

	@Override
	public String toString() {
		return "ArcGISResponseDto [totalLength=" + totalLength + ", totalTime=" + totalTime + ", totalDriveTime="
				+ totalDriveTime + ", responseMessage=" + responseMessage + "]";
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

}
