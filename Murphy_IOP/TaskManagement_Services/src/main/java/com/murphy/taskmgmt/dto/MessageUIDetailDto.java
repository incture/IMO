package com.murphy.taskmgmt.dto;

public class MessageUIDetailDto {

	private MessageDto message;
	private ResponseMessage responseMessage;

	public MessageDto getMessage() {
		return message;
	}

	public void setMessage(MessageDto message) {
		this.message = message;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "MessageUIDetailDto [message=" + message + ", responseMessage=" + responseMessage + "]";
	}

}
