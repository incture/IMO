package com.murphy.taskmgmt.dto;

import java.util.List;

public class CollaborationResponseDto {
	
	private ResponseMessage responseMessage;
	private List<CollaborationDto> collaborationDtos;
	
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<CollaborationDto> getCollaborationDtos() {
		return collaborationDtos;
	}
	public void setCollaborationDtos(List<CollaborationDto> collaborationDtos) {
		this.collaborationDtos = collaborationDtos;
	}
	
	
	@Override
	public String toString() {
		return "CollaborationResponseDto [responseMessage=" + responseMessage + ", collaborationDtos="
				+ collaborationDtos + "]";
	}
	
	
	
	
}
