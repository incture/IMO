package com.murphy.taskmgmt.dto;

public class TeamsResponseDto {

	private String type;
	private String text;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TeamsResponseDto [type=" + type + ", text=" + text + "]";
	}

}
