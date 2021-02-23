package com.murphy.downtime.dto;

public class DowntimeCaptureFetchResponseDto {

	private ResponseMessage responseMessage;
	private DowntimeCaptureDto downtimeCaptureDto;
	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public DowntimeCaptureDto getDowntimeCaptureDto() {
		return downtimeCaptureDto;
	}
	public void setDowntimeCaptureDto(DowntimeCaptureDto downtimeCaptureDto) {
		this.downtimeCaptureDto = downtimeCaptureDto;
	}
	
	@Override
	public String toString() {
		return "DowntimeCaptureFetchResponseDto [responseMessage=" + responseMessage + ", downtimeCaptureDto=" + downtimeCaptureDto + "]";
	}
}
