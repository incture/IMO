package com.murphy.taskmgmt.dto;

import java.util.List;
import java.util.Map;

public class SsdBypassLogListResponseDto {
	private List<SsdBypassListDto> ssdBypassListDto;
	private ResponseMessage ResponseMessage;
	private Integer totalCount;
	private List<Map<String, Object>> statusCountList;

	/**
	 * @return the ssdBypassListDto
	 */
	public List<SsdBypassListDto> getSsdBypassListDto() {
		return ssdBypassListDto;
	}

	/**
	 * @param ssdBypassListDto
	 *            the ssdBypassListDto to set
	 */
	public void setSsdBypassListDto(List<SsdBypassListDto> ssdBypassListDto) {
		this.ssdBypassListDto = ssdBypassListDto;
	}

	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return ResponseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		ResponseMessage = responseMessage;
	}

	/**
	 * @return the totalCount
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the statusCountList
	 */
	public List<Map<String, Object>> getStatusCountList() {
		return statusCountList;
	}

	/**
	 * @param statusCountList
	 *            the statusCountList to set
	 */
	public void setStatusCountList(List<Map<String, Object>> statusCountList) {
		this.statusCountList = statusCountList;
	}

}
