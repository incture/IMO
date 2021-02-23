package com.murphy.taskmgmt.dto;

import java.util.List;

public class WellVisitMatrixDto {

	//5 matrix to represent 5 days
	private List<WellVisitDayMatrixDto> DayMatrixList;
	private ResponseMessage responseMessage;

	public List<WellVisitDayMatrixDto> getDayMatrixList() {
		return DayMatrixList;
	}

	public void setDayMatrixList(List<WellVisitDayMatrixDto> dayMatrixList) {
		DayMatrixList = dayMatrixList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		return "WellVisitMatrixDto [DayMatrixList=" + DayMatrixList + ", responseMessage=" + responseMessage + "]";
	}
	
}
