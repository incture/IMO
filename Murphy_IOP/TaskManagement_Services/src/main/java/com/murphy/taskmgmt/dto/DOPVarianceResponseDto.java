package com.murphy.taskmgmt.dto;

import java.util.List;

public class DOPVarianceResponseDto {

	private List<DOPVarianceDto> dopVarianceDtoList;
	private ResponseMessage responseMessage;
	private String dataAsOffDisplay;
	private String locationType;
	private long dataAsOffDisplayEpoch;
	
	
	
	
	
	public long getDataAsOffDisplayEpoch() {
		return dataAsOffDisplayEpoch;
	}
	public void setDataAsOffDisplayEpoch(long dataAsOffDisplayEpoch) {
		this.dataAsOffDisplayEpoch = dataAsOffDisplayEpoch;
	}
	public List<DOPVarianceDto> getDopVarianceDtoList() {
		return dopVarianceDtoList;
	}
	public void setDopVarianceDtoList(List<DOPVarianceDto> dopVarianceDtoList) {
		this.dopVarianceDtoList = dopVarianceDtoList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "DOPVarianceResponseDto [dopVarianceDtoList=" + dopVarianceDtoList + ", responseMessage="
				+ responseMessage + ", dataAsOffDisplay=" + dataAsOffDisplay + ", locationType=" + locationType
				+ ", dataAsOffDisplayEpoch=" + dataAsOffDisplayEpoch + "]";
	}
	public String getDataAsOffDisplay() {
		return dataAsOffDisplay;
	}
	public void setDataAsOffDisplay(String dataAsOffDisplay) {
		this.dataAsOffDisplay = dataAsOffDisplay;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	
	
}
