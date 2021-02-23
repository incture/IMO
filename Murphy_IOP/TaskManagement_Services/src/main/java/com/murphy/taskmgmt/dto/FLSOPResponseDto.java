package com.murphy.taskmgmt.dto;

public class FLSOPResponseDto {
	
	private String flsop;
	private ResponseMessage responseMessage;
	
	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getFlsop() {
		return flsop;
	}
	public void setFlsop(String flsop) {
		this.flsop = flsop;
	}

	@Override
	public String toString() {
		return "FLSOPResponseDto [flsop=" + flsop + ", responseMessage=" + responseMessage + "]";
	}
	
	
}
