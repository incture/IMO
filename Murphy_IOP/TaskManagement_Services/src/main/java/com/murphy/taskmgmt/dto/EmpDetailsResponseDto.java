package com.murphy.taskmgmt.dto;

public class EmpDetailsResponseDto {
  
	private EmpDetailsDto empDetailsDto;
	private ResponseMessage responseMessage;
//	private String userEmail;
//	private String userLoginName;
	
	
	public EmpDetailsDto getEmpDetailsDto() {
		return empDetailsDto;
	}
	public void setEmpDetailsDto(EmpDetailsDto empDetailsDto) {
		this.empDetailsDto = empDetailsDto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "EmpDetailsResponseDto [empDetailsDto=" + empDetailsDto + ", responseMessage=" + responseMessage + "]";
	}
}
