package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class AttachmentDto extends BaseDto{
	
	private String fileId;
	private String fileName;
	private String fileType;
	private String mappingId;
	private String fileDoc;
	private String compressedFile;
	private String documentId;
	private String documentUrl;
	
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	public String getMappingId() {
		return mappingId;
	}
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
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
	@Override
	public String toString() {
		return "AttachmentDto [fileId=" + fileId + ", fileName=" + fileName + ", fileType=" + fileType + ", mappingId="
				+ mappingId + ", fileDoc=" + fileDoc + ", compressedFile=" + compressedFile + ", documentId="
				+ documentId + ", documentUrl=" + documentUrl + "]";
	}
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		
	}
	public String getCompressedFile() {
		return compressedFile;
	}
	public void setCompressedFile(String compressedFile) {
		this.compressedFile = compressedFile;
	}
	public String getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}
	
	
}
