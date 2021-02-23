package com.murphy.taskmgmt.entity;

import java.util.Arrays;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "APP_FILE")
public class AppFileDo implements BaseDo {

	@Id
	@Column(name = "ID")
	private String id = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_TYPE")
	private String fileType;

	@Column(name = "FILE_VERSION")
	private String fileVersion;

	@Column(name = "FILE_URL")
	private String fileUrl;

	@Lob
	@Column(name = "FILE")
	private byte[] file;

	@Column(name = "APPLICATION")
	private String application;
	
	@Column(name = "OS_VERSION")
	private String osVersion;
	

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

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

	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "AppFileDo [id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", fileVersion="
				+ fileVersion + ", fileUrl=" + fileUrl + ", file=" + Arrays.toString(file) + ", application="
				+ application + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
