package com.murphy.datamaintenance.dto;

import com.murphy.taskmgmt.dto.ResponseMessage;

public class DataMaintenanceResponseDto {
	private FileDetailsDto fileDetailsDto;
	private ResponseMessage message;
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	public FileDetailsDto getFileDetailsDto() {
		return fileDetailsDto;
	}
	public void setFileDetailsDto(FileDetailsDto fileDetailsDto) {
		this.fileDetailsDto = fileDetailsDto;
	}
}
