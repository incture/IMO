package com.murphy.integration.dto;

import java.util.List;

public class DowntimeCaptureFetchResponseDto {

	private ResponseMessage responseMessage;
	private DowntimeCaptureDto downtimeCaptureDto;
	private List<DowntimeCaptureDto> dcDtoList;
	private int totalCount;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
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
		return "DowntimeCaptureFetchResponseDto [responseMessage=" + responseMessage + ", downtimeCaptureDto="
				+ downtimeCaptureDto + ", dcDtoList=" + dcDtoList + ", totalCount=" + totalCount + "]";
	}
	public List<DowntimeCaptureDto> getDcDtoList() {
		return dcDtoList;
	}
	public void setDcDtoList(List<DowntimeCaptureDto> dcDtoList) {
		this.dcDtoList = dcDtoList;
	}
}
