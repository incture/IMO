package com.murphy.taskmgmt.dto;

public class QRCodeDto {

	private String qrCode;
	private ResponseMessage message;
	
	
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	
	
	
}
