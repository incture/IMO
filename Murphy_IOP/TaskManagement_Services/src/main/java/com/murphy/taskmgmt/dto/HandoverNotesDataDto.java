package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class HandoverNotesDataDto extends BaseDto{
	
	private String note;
	private Integer noteId;
	private String noteCategoryId;
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getNoteId() {
		return noteId;
	}
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	public String getNoteCategoryId() {
		return noteCategoryId;
	}
	public void setNoteCategoryId(String noteCategoryId) {
		this.noteCategoryId = noteCategoryId;
	}
	@Override
	public String toString() {
		return "HandoverNotesDataDto [note=" + note + ", noteId=" + noteId + ", noteCategoryId=" + noteCategoryId + "]";
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
