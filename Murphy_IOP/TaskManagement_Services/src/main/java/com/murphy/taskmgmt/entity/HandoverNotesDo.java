package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "HANDOVER_NOTES")
public class HandoverNotesDo implements BaseDo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTE_GENERATOR")
	@SequenceGenerator(name = "NOTE_GENERATOR", sequenceName = "NOTE_ID_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "NOTE_ID", nullable = false)
	private Integer noteId;

	@Temporal(javax.persistence.TemporalType.DATE)
	@Column(name = "DATE", nullable = false)
	private Date date;

	@Column(name = "SHIFT", nullable = false)
	private String shift;

	@Column(name = "FIELD", nullable = false)
	private String field;

	@Column(name = "USER_ID", nullable = false)
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
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

	@Override
	public String toString() {
		return "HandoverNotesDo [noteId=" + noteId + ", date=" + date + ", shift=" + shift + ", field=" + field
				+ ", userId=" + userId + ", userName=" + userName + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
