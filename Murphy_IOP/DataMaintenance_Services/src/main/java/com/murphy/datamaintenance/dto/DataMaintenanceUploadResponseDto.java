package com.murphy.datamaintenance.dto;

import java.util.List;

import com.murphy.taskmgmt.dto.ResponseMessage;

public class DataMaintenanceUploadResponseDto {
	private List<String> errorMesssage;
	private ResponseMessage message;
	public List<String> getErrorMesssage() {
		return errorMesssage;
	}
	public void setErrorMesssage(List<String> errorMesssage) {
		this.errorMesssage = errorMesssage;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	

}
