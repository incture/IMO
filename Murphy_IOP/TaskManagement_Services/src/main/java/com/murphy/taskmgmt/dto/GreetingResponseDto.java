package com.murphy.taskmgmt.dto;

public class GreetingResponseDto {

	private String firstName;
	private String lastName;
	private Boolean isGreeted;
	private ResponseMessage responseMessage;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getIsGreeted() {
		return isGreeted;
	}

	public void setIsGreeted(Boolean isGreeted) {
		this.isGreeted = isGreeted;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "GreetingResponseDto [firstName=" + firstName + ", lastName=" + lastName + ", isGreeted=" + isGreeted
				+ "]";
	}

}
