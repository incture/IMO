package com.murphy.taskmgmt.dto;

import java.util.List;

public class FracPackEngViewResponseDto {

	private List<FracPackEngViewDto>  fracPacks;
	
	private ResponseMessage responseMessage;

	public List<FracPackEngViewDto> getFracPacks() {
		return fracPacks;
	}

	public void setFracPacks(List<FracPackEngViewDto> fracPacks) {
		this.fracPacks = fracPacks;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
	
	
}
