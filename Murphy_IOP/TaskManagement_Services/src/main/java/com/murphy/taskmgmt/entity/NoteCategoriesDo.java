package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "NOTE_CATEGORIES")
@Entity
public class NoteCategoriesDo implements BaseDo {

	@Id
	@Column(name = "NOTE_CAT_ID",nullable=false)
	private String noteCategoryId;

	@Column(name = "NOTE_CAT_DESC",length=100)
	private String noteCategoryDescription;

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

	@Override
	public String toString() {
		return "NoteCategoriesDo [noteCategoryId=" + noteCategoryId + ", noteCategoryDescription="
				+ noteCategoryDescription + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return noteCategoryId;
	}

}
