/**
 * 
 */
package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Kamlesh.Choubey
 *
 */

@Entity
@Table(name = "SSD_BYPASS_ATTACHMENTS")
public class SsdBypassAttachmentsDo implements BaseDo {

	@Id
	@Column(name = "DOCUMENT_ID", length = 100)
	private String documentId = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "FILE_NAME", length = 100)
	private String fileName;

	@Column(name = "FILE_TYPE", length = 30)
	private String fileType;

	@Column(name = "FILE_ID", length = 100)
	private String fileId;

	@Column(name = "BYPASS_ID", length = 100)
	private String bypassId;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();

	@Column(name = "ATTACHMENT_URL", length = 150)
	private String attachmentUrl;

	/*
	 * @Column(name = "FILE_DOC", length = 10000) private String fileDoc;
	 */

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
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
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
	/*
		*//**
			 * @return the fileDoc
			 */
	/*
	 * public String getFileDoc() { return fileDoc; }
	 * 
	 *//**
		 * @param fileDoc
		 *            the fileDoc to set
		 *//*
		 * public void setFileDoc(String fileDoc) { this.fileDoc = fileDoc; }
		 */
}
