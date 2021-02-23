package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HandoverNotesDataPKKeys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3965195486190647138L;

	@Column(name = "NOTE_ID")
	private Integer noteId;

	@Column(name = "NOTE_CAT_ID")
	private String noteCategoryId;

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

}
