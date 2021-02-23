package com.murphy.taskmgmt.dto;

import java.util.List;

public class ObxWorkLoadDetailsResponseDto {
	
	private ResponseMessage responseMessage;
	private List<ObxOperatorWorkloadDetailsDto> obxWorkLoadList;
	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the obxWorkLoadList
	 */
	public List<ObxOperatorWorkloadDetailsDto> getObxWorkLoadList() {
		return obxWorkLoadList;
	}
	/**
	 * @param obxWorkLoadList the obxWorkLoadList to set
	 */
	public void setObxWorkLoadList(List<ObxOperatorWorkloadDetailsDto> obxWorkLoadList) {
		this.obxWorkLoadList = obxWorkLoadList;
	}

}
