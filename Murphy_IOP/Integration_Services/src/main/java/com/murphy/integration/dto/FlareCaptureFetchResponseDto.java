package com.murphy.integration.dto;

import java.util.List;

public class FlareCaptureFetchResponseDto {
	
	private ResponseMessage responseMessage;
	private FlareDowntimeCaptureDto flareDowntimeCaptureDto;
	private List<FlareDowntimeCaptureDto> dcDtoList;
	private int totalCount;
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public FlareDowntimeCaptureDto getFlareDowntimeCaptureDto() {
		return flareDowntimeCaptureDto;
	}
	public void setFlareDowntimeCaptureDto(FlareDowntimeCaptureDto flareDowntimeCaptureDto) {
		this.flareDowntimeCaptureDto = flareDowntimeCaptureDto;
	}
	public List<FlareDowntimeCaptureDto> getDcDtoList() {
		return dcDtoList;
	}
	public void setDcDtoList(List<FlareDowntimeCaptureDto> dcDtoList) {
		this.dcDtoList = dcDtoList;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	@Override
	public String toString() {
		return "FlareCaptureFetchResponseDto [responseMessage=" + responseMessage + ", flareDowntimeCaptureDto="
				+ flareDowntimeCaptureDto + ", dcDtoList=" + dcDtoList + ", totalCount=" + totalCount + "]";
	}
	
}
