package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class AttachmentResponseDto extends BaseDto{
	
	private AttachmentDto dto;
	private ResponseMessage message;
	
	public AttachmentDto getDto() {
		return dto;
	}
	public void setDto(AttachmentDto dto) {
		this.dto = dto;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "AttachmentResponseDto [dto=" + dto + ", message=" + message + "]";
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
}
