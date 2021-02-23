package com.murphy.taskmgmt.dto;

import java.util.List;

public class PWHopperResponseDto  {

	private List<PWHopperDto> hopperDtoList;
	private ResponseMessage responseMessage;
	
	public List<PWHopperDto> getHopperDtoList() {
		return hopperDtoList;
	}
	public void setHopperDtoList(List<PWHopperDto> hopperDtoList) {
		this.hopperDtoList = hopperDtoList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		return "PWHopperResponseDto [hopperDtoList=" + hopperDtoList + ", responseMessage=" + responseMessage + "]";
	}
	
}


