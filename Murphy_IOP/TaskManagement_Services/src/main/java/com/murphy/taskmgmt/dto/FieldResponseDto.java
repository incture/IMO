package com.murphy.taskmgmt.dto;

public class FieldResponseDto	{
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
	@Override
	public String toString() {
		return "FieldResponseDto [locationText=" + locationText + ", locationCode=" + locationCode + ", field=" + field
				+ ", responseMessage=" + responseMessage + "]";
	}


	public String getLocationText() {
		return locationText;
	}
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}


	private String locationText;
	private String locationCode;
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}


	private String field;
	private ResponseMessage responseMessage;
	
}
