package com.murphy.taskmgmt.dto;

public class PigUpdateServiceDto {

	private String documentUrl;
	private String fileName;
	private String workOrderNo;
	private String status;
	private String actualResolveTime;
	
	public String getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getActualResolveTime() {
		return actualResolveTime;
	}
	public void setActualResolveTime(String actualResolveTime) {
		this.actualResolveTime = actualResolveTime;
	}
	@Override
	public String toString() {
		return "PigUpdateServiceDto [documentUrl=" + documentUrl + ", fileName=" + fileName + ", workOrderNo="
				+ workOrderNo + ", status=" + status + ", actualResolveTime=" + actualResolveTime + "]";
	}
	
	
	
}
