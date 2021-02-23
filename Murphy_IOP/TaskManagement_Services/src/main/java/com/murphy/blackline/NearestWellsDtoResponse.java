package com.murphy.blackline;

import java.util.List;

import com.murphy.taskmgmt.dto.ResponseMessage;

public class NearestWellsDtoResponse {
	
	private List<NearestWellsDto> NearestWellsDto;
	@Override
	public String toString() {
		return "NearestWellsDtoResponse [NearestWellsDto=" + NearestWellsDto + ", responseMessage=" + responseMessage
				+ "]";
	}
	public List<NearestWellsDto> getNearestWellsDto() {
		return NearestWellsDto;
	}
	public void setNearestWellsDto(List<NearestWellsDto> nearestWellsDto) {
		NearestWellsDto = nearestWellsDto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	ResponseMessage responseMessage;

}
