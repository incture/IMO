package com.murphy.taskmgmt.dto;

public class SendMailDto {

	private String receipentId;
	private String cc;
	private String subjectName;
	private String message;
	private String userName;

	public String getReceipentId() {
		return receipentId;
	}

	public void setReceipentId(String receipentId) {
		this.receipentId = receipentId;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "SendMailDto [receipentId=" + receipentId + ", cc=" + cc + ", subjectName=" + subjectName + ", message="
				+ message + ", userName=" + userName + "]";
	}

}
