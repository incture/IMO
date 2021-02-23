package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TM_ATTACHMENT")
public class AttachmentDo implements BaseDo, Serializable {

	public String getDocumentId() {
		return documentId;
	}


	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FILE_ID", length = 32)
	private String fileId=UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "FILE_NAME", length = 70)
	private String fileName;
	
	@Column(name = "FILE_TYPE", length = 20)
	private String fileType;
	
	@Column(name = "MAPPING_ID", length = 32)
	private String mappingId;
	
	@Lob
	@Column(name = "FILE_DOC")
	private byte[] fileDoc;
	
	@Lob
	@Column(name = "COMPRESSED_FILE")
	private byte[] compressedFile;
	
	@Column(name = "DOCUMENT_ID")
	private String documentId;

	public byte[] getCompressedFile() {
		return compressedFile;
	}


	public void setCompressedFile(byte[] compressedFile) {
		this.compressedFile = compressedFile;
	}


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


	public byte[] getFileDoc() {
		return fileDoc;
	}


	public void setFileDoc(byte[] fileDoc) {
		this.fileDoc = fileDoc;
	}

	@Override
	public String toString() {
		return "AttachmentDo [fileId=" + fileId + ", fileName=" + fileName + ", fileType=" + fileType + ", mappingId="
				+ mappingId + ", fileDoc=" + Arrays.toString(fileDoc) + ", compressedFile="
				+ Arrays.toString(compressedFile) + ", documentId=" + documentId + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return fileId;
	}

}
