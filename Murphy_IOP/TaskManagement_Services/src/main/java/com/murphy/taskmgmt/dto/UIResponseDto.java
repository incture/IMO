package com.murphy.taskmgmt.dto;

public class UIResponseDto{
	
	private int  count;
	private ResponseMessage responseMessage;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		return "UIResponseDto [count=" + count + ", responseMessage=" + responseMessage + "]";
	}
	
	
	
	
	
	
}
