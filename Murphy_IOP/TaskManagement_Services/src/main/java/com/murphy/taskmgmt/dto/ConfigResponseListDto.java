package com.murphy.taskmgmt.dto;

import java.util.HashMap;

public class ConfigResponseListDto {

	private HashMap<String, String> responseConfigMap;
	private ResponseMessage responseMessage;

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
	 * @return the responseConfigMap
	 */
	public HashMap<String, String> getResponseConfigMap() {
		return responseConfigMap;
	}

	/**
	 * @param responseConfigMap the responseConfigMap to set
	 */
	public void setResponseConfigMap(HashMap<String, String> responseConfigMap) {
		this.responseConfigMap = responseConfigMap;
	}
}
