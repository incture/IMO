package com.murphy.taskmgmt.dto;

import java.util.ArrayList;
import java.util.List;

public class ArcGISBestSequesnceListDto {

	private Double cumulativeDriveTime;
	private List<ArcGISBestSequesnceDto> bestSequenceList;
	private ResponseMessage responseMessage;
	
	public ArcGISBestSequesnceListDto(){
		this.bestSequenceList=new ArrayList<ArcGISBestSequesnceDto>();
	}
	public List<ArcGISBestSequesnceDto> getBestSequenceList() {
		return bestSequenceList;
	}
	public void setBestSequenceList(List<ArcGISBestSequesnceDto> bestSequenceList) {
		this.bestSequenceList = bestSequenceList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public Double getCumulativeDriveTime() {
		return cumulativeDriveTime;
	}
	public void setCumulativeDriveTime(Double cumulativeDriveTime) {
		this.cumulativeDriveTime = cumulativeDriveTime;
	}
	@Override
	public String toString() {
		return "ArcGISBestSequesnceListDto [cumulativeDriveTime=" + cumulativeDriveTime + ", bestSequenceList="
				+ bestSequenceList + ", responseMessage=" + responseMessage + "]";
	}
	
}
