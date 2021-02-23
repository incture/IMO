package com.murphy.taskmgmt.dto;

import java.util.List;

public class ShiftRegisterResponseDto {

	private ResponseMessage responseMessage;
	private List<EmpShiftDetailsDto> EmpShiftDetailsDto;
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<EmpShiftDetailsDto> getEmpShiftDetailsDto() {
		return EmpShiftDetailsDto;
	}
	public void setEmpShiftDetailsDto(List<EmpShiftDetailsDto> empShiftDetailsDto) {
		EmpShiftDetailsDto = empShiftDetailsDto;
	}
}
