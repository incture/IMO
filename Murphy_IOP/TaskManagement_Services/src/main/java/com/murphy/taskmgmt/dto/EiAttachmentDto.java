package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EiAttachmentDto extends BaseDto {

	private String id;
	private String fileName;
	private String fileType;
	private String fileId;
	private String formId;
	private String isolationId;
	private String createdBy;
	private Date createdAt;
	private String attachmentUrl;
	private String fileDoc;
	
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
	public String getFileDoc() {
		return fileDoc;
	}
	public void setFileDoc(String fileDoc) {
		this.fileDoc = fileDoc;
	}
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	@Override
	public String toString() {
		return "EiAttachmentDto [id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", fileId=" + fileId
				+ ", formId=" + formId + ", isolationId=" + isolationId + ", createdBy=" + createdBy + ", createdAt="
				+ createdAt + ", attachmentUrl=" + attachmentUrl + ", fileDoc=" + fileDoc + "]";
	}
	
}
