package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EI_ATTACHMENT")
public class EIAttachmentDo implements BaseDo {

	@Id
	@Column(name = "ID", length = 100)
	private String id;

	@Column(name = "FILE_NAME", length = 100)
	private String fileName;

	@Column(name = "FILE_TYPE", length = 30)
	private String fileType;

	@Column(name = "FILE_ID", length = 100)
	private String fileId;

	@Column(name = "FORM_ID", length = 100)
	private String formId;
	
	@Column(name = "ISOLATION_ID", length = 100)
	private String isolationId;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "ATTACHMENT_URL", length = 150)
	private String attachmentUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getIsolationId() {
		return isolationId;
	}

	public void setIsolationId(String isolationId) {
		this.isolationId = isolationId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
