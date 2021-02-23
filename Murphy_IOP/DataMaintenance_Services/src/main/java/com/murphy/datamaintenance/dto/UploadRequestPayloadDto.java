package com.murphy.datamaintenance.dto;

public class UploadRequestPayloadDto {
	private RequestPayloadDto requestPayloadDto;
	private FileDetailsDto fileDetailsDto;
	public RequestPayloadDto getRequestPayloadDto() {
		return requestPayloadDto;
	}
	public void setRequestPayloadDto(RequestPayloadDto requestPayloadDto) {
		this.requestPayloadDto = requestPayloadDto;
	}
	public FileDetailsDto getFileDetailsDto() {
		return fileDetailsDto;
	}
	public void setFileDetailsDto(FileDetailsDto fileDetailsDto) {
		this.fileDetailsDto = fileDetailsDto;
	}

}
