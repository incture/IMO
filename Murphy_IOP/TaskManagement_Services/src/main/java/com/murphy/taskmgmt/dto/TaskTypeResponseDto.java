package com.murphy.taskmgmt.dto;

import java.util.List;

public class TaskTypeResponseDto {


	private List<CustomAttrValuesDto> valueDtos;
	private ResponseMessage responseMessage;
	



	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}



	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}



	@Override
	public String toString() {
		return "TaskTypeResponseDto [valueDtos=" + valueDtos + ", responseMessage=" + responseMessage + "]";
	}



	public List<CustomAttrValuesDto> getValueDtos() {
		return valueDtos;
	}



	public void setValueDtos(List<CustomAttrValuesDto> valueDtos) {
		this.valueDtos = valueDtos;
	}
	
	
}
