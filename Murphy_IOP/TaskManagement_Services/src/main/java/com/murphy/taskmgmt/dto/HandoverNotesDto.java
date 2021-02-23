package com.murphy.taskmgmt.dto;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class HandoverNotesDto extends BaseDto{

	
	private String noteCategoryId;
	private String noteCategoryDescription;
	private Integer noteId;
	private String note;
	private String shift;
	private String field;
	private String userId;
	private String userName;
	private Date noteCreatedAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String date;

	public String getNoteCategoryId() {
		return noteCategoryId;
	}
	public void setNoteCategoryId(String noteCategoryId) {
		this.noteCategoryId = noteCategoryId;
	}
	public String getNoteCategoryDescription() {
		return noteCategoryDescription;
	}
	public void setNoteCategoryDescription(String noteCategoryDescription) {
		this.noteCategoryDescription = noteCategoryDescription;
	}
	public Integer getNoteId() {
		return noteId;
	}
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	
	
	public Date getNoteCreatedAt() {
		return noteCreatedAt;
	}
	public void setNoteCreatedAt(Date noteCreatedAt) {
		this.noteCreatedAt = noteCreatedAt;
	}
	@Override
	public String toString() {
		return "HandoverNotesDto [noteCategoryId=" + noteCategoryId + ", noteCategoryDescription="
				+ noteCategoryDescription + ", noteId=" + noteId + ", note=" + note + ", date=" + date + ", shift="
				+ shift + ", field=" + field + ", userId=" + userId + ", userName=" + userName + ", noteCreatedAt="
				+ noteCreatedAt + "]";
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
}
