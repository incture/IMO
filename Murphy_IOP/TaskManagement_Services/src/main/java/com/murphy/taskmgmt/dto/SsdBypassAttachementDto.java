package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class SsdBypassAttachementDto extends BaseDto {

	private String documentId;
	private String fileName;
	private String fileType;
	private String fileId;
	private String bypassId;
	private String createdBy;
	private Date createdAt;
	private String attachmentUrl;
	private String fileDoc;

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId
	 *            the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the bypassId
	 */
	public String getBypassId() {
		return bypassId;
	}

	/**
	 * @param bypassId
	 *            the bypassId to set
	 */
	public void setBypassId(String bypassId) {
		this.bypassId = bypassId;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the attachmentUrl
	 */
	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	/**
	 * @param attachmentUrl
	 *            the attachmentUrl to set
	 */
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	/**
	 * @return the fileDoc
	 */
	public String getFileDoc() {
		return fileDoc;
	}

	/**
	 * @param fileDoc
	 *            the fileDoc to set
	 */
	public void setFileDoc(String fileDoc) {
		this.fileDoc = fileDoc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SsdBypassAttachementDto [documentId=" + documentId + ", fileName=" + fileName + ", fileType=" + fileType
				+ ", fileId=" + fileId + ", bypassId=" + bypassId + ", createdBy=" + createdBy + ", createdAt="
				+ createdAt + ", attachmentUrl=" + attachmentUrl + ", fileDoc=" + fileDoc + ", isNullable=" + isNullable
				+ ", getDocumentId()=" + getDocumentId() + ", getFileName()=" + getFileName() + ", getFileType()="
				+ getFileType() + ", getFileId()=" + getFileId() + ", getBypassId()=" + getBypassId()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedAt()=" + getCreatedAt() + ", getAttachmentUrl()="
				+ getAttachmentUrl() + ", getFileDoc()=" + getFileDoc() + ", getValidForUsage()=" + getValidForUsage()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
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
