package com.murphy.taskmgmt.dto;

import java.util.List;

public class ReasonForBypassResponseListDto {

	private ResponseMessage responseMessage;
	private List<ReasonForBypassDto> reasonForBypassDtoList;

	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * @return the reasonForBypassDtoList
	 */
	public List<ReasonForBypassDto> getReasonForBypassDtoList() {
		return reasonForBypassDtoList;
	}

	/**
	 * @param reasonForBypassDtoList
	 *            the reasonForBypassDtoList to set
	 */
	public void setReasonForBypassDtoList(List<ReasonForBypassDto> reasonForBypassDtoList) {
		this.reasonForBypassDtoList = reasonForBypassDtoList;
	}

}
