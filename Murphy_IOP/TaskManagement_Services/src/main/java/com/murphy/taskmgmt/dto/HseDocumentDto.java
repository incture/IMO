package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class HseDocumentDto extends BaseDto {
	private String stringId;
	private String docId;
	private String documentId;
	private String docName;
	private String docType;	
	private int docVersion;	
	private Date createdAt;
	private String attachmentUrl;
	private String fileDoc;
	private String searchString;
	private int stringCount;
	
	
	
	
	public int getStringCount() {
		return stringCount;
	}
	public void setStringCount(int stringCount) {
		this.stringCount = stringCount;
	}
	public String getStringId() {
		return stringId;
	}
	public void setStringId(String stringId) {
		this.stringId = stringId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getFileDoc() {
		return fileDoc;
	}
	public void setFileDoc(String fileDoc) {
		this.fileDoc = fileDoc;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public int getDocVersion() {
		return docVersion;
	}
	public void setDocVersion(int docVersion) {
		this.docVersion = docVersion;
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
	public String toString() {
		return "HSEDocumentDto [docId=" + docId + ", docName=" + docName + ", docType=" + docType
				+ ", docVersion=" + docVersion +", createdAt=" + createdAt
				+ ", attachmentUrl=" + attachmentUrl + "]";
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
