package com.murphy.taskmgmt.dto;

public class DocServiceDto {

	private String fileName;
	private String fileType;
	private String fileContent;
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
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	@Override
	public String toString() {
		return "DocServiceDto [fileName=" + fileName + ", fileType=" + fileType + ", fileContent=" + fileContent + "]";
	}
	
	
}
