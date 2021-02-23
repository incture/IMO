package com.murphy.taskmgmt.dto;

import java.util.List;

public class FieldAvailabilityResponseDto {
	private List<FieldAvailabilityDto> fieldAvailabilityDtoList;
	private double cumulativePercentage;
	private String cumulativePercent;
	private ResponseMessage responseMessage;
	public double getCumulativePercentage() {
		return cumulativePercentage;
	}

	public void setCumulativePercentage(double cumulativePercentage) {
		this.cumulativePercentage = cumulativePercentage;
	}

	public String getCumulativePercent() {
		return cumulativePercent;
	}

	public void setCumulativePercent(String cumulativePercent) {
		this.cumulativePercent = cumulativePercent;
	}



	public List<FieldAvailabilityDto> getFieldAvailabilityDtoList() {
		return fieldAvailabilityDtoList;
	}

	public void setFieldAvailabilityDtoList(List<FieldAvailabilityDto> fieldAvailabilityDtoList) {
		this.fieldAvailabilityDtoList = fieldAvailabilityDtoList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

}
