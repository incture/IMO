package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "HANDOVER_NOTES_DATA")

public class HandoverNotesDataDo implements Serializable,BaseDo {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private HandoverNotesDataPKKeys handoverNotesDataPKKeys;

    @Lob
	@Column(name = "NOTE")
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	public HandoverNotesDataPKKeys getHandoverNotesDataPKKeys() {
		return handoverNotesDataPKKeys;
	}

	public void setHandoverNotesDataPKKeys(HandoverNotesDataPKKeys handoverNotesDataPKKeys) {
		this.handoverNotesDataPKKeys = handoverNotesDataPKKeys;
	}


	@Override
	public String toString() {
		return "HandoverNotesDataDo [handoverNotesDataPKKeys=" + handoverNotesDataPKKeys + ", note=" + note + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
