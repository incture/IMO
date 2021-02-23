package com.murphy.taskmgmt.reports;

import com.murphy.integration.dto.UIRequestDto;

public class ReportPayload {
	
	private String reportName;
	private String fileFormate;
	private UIRequestDto uiRequestDto;
	private String date;
	private String status;
	private String taskType;
	private String parentOrigin;
	private int durationInMonths;
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getParentOrigin() {
		return parentOrigin;
	}

	public void setParentOrigin(String parentOrigin) {
		this.parentOrigin = parentOrigin;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UIRequestDto getUiRequestDto() {
		return uiRequestDto;
	}

	public void setUiRequestDto(UIRequestDto uiRequestDto) {
		this.uiRequestDto = uiRequestDto;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getFileFormate() {
		return fileFormate;
	}

	public void setFileFormate(String fileFormate) {
		this.fileFormate = fileFormate;
	}

	public int getDurationInMonths() {
		return durationInMonths;
	}

	public void setDurationInMonths(int durationInMonths) {
		this.durationInMonths = durationInMonths;
	}

	@Override
	public String toString() {
		return "ReportPayload [reportName=" + reportName + ", fileFormate=" + fileFormate + ", uiRequestDto="
				+ uiRequestDto + ", date=" + date + ", status=" + status + ", taskType=" + taskType + ", parentOrigin="
				+ parentOrigin + ", durationInMonths=" + durationInMonths + "]";
	}

	
}
