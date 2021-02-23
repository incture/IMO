package com.murphy.taskmgmt.dto;

public class FileVersionDto {

	private String fileType;
	private String fileVersion;
	private String application;

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

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	@Override
	public String toString() {
		return "FileVersionDto [fileType=" + fileType + ", fileVersion=" + fileVersion + ", application=" + application
				+ "]";
	}

}
