package com.murphy.taskmgmt.dto;

public class LocationResponseDto {

	
	
	private HierarchyResponseDto dto;
	private ResponseMessage message;
	
	public HierarchyResponseDto getDto() {
		return dto;
	}
	public void setDto(HierarchyResponseDto dto) {
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
		return "LocationResponseDto [dto=" + dto + ", message=" + message + "]";
	}
	
	}
