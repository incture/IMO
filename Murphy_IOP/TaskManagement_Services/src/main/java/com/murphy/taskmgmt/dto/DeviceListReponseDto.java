package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.dto.DeviceDto;

public class DeviceListReponseDto {
	
	private List<DeviceDto> deviceDtoList;
	ResponseMessage responseMessage;
	/**
	 * @return the deviceDtoList
	 */
	public List<DeviceDto> getDeviceDtoList() {
		return deviceDtoList;
	}
	/**
	 * @param deviceDtoList the deviceDtoList to set
	 */
	public void setDeviceDtoList(List<DeviceDto> deviceDtoList) {
		this.deviceDtoList = deviceDtoList;
	}
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
	
	

}
