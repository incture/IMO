package com.murphy.taskmgmt.dto;

import java.util.List;

public class RiskListResponseDto {
	
	private List<BypassRiskLevelDto> dto;
	private ResponseMessage responseMessage;
	public List<BypassRiskLevelDto> getDto() {
		return dto;
	}
	public void setDto(List<BypassRiskLevelDto> dto) {
		this.dto = dto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "RiskListResponseDto [dto=" + dto + ", responseMessage=" + responseMessage + "]";
	}
	
}
