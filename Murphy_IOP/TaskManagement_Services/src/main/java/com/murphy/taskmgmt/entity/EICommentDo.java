package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EI_COMMENT")
public class EICommentDo implements BaseDo {
	@Id
	@Column(name = "ID", length = 100)
	private String id = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "EI_FORM_ID", length = 100)
	private String formId;

	@Column(name = "COMMENT", length = 1000)
	private String comment;

	@Column(name = "UPDATED_BY", length = 100)
	private String updatedBy;

	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}
}
