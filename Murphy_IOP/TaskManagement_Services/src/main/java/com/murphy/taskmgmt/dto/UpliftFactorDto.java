package com.murphy.taskmgmt.dto;

public class UpliftFactorDto {
	private Double value;
	private String icon;
	private ResponseMessage message;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "UpliftFactorDto [value=" + value + ", icon=" + icon + ", message=" + message.toString() + "]";
	}

}
