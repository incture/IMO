package com.murphy.taskmgmt.dto;

public class HandoverNotesRequestDto {
	
	private String fromDate;
	private String toDate;
	private String shift;
	private String field;
	private String userId;
	private String userName;
	private String noteType;
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	@Override
	public String toString() {
		return "HandoverNotesRequestDto [fromDate=" + fromDate + ", toDate=" + toDate + ", shift=" + shift + ", field="
				+ field + ", userId=" + userId + ", userName=" + userName + ", noteType=" + noteType + "]";
	}
	
	

}
