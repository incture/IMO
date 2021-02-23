package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

public class CygnetAlarmFeedResponseDto {

	private List<CygnetAlarmFeedDto> alarmsList;
	private ResponseMessage responseMessage;
	private BigDecimal totalCount;
	private BigDecimal pageCount;

	public List<CygnetAlarmFeedDto> getAlarmsList() {
		return alarmsList;
	}

	public void setAlarmsList(List<CygnetAlarmFeedDto> alarmsList) {
		this.alarmsList = alarmsList;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public BigDecimal getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	public BigDecimal getPageCount() {
		return pageCount;
	}

	public void setPageCount(BigDecimal pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "CygnetAlarmFeedResponseDto [alarmsList=" + alarmsList + ", responseMessage=" + responseMessage
				+ ", totalCount=" + totalCount + ", pageCount=" + pageCount + "]";
	}

}
