package com.murphy.integration.dto;

public class ResponseMessage {
	
	private String status;
	private String statusCode;
	private String message;
	private Object data;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ResponseMessage [status=" + status + ", statusCode=" + statusCode + ", message=" + message + ", data="
				+ data + "]";
	}


}
