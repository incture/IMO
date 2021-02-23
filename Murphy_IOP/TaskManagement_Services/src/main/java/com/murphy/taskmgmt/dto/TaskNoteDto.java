package com.murphy.taskmgmt.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TaskNoteDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String wellName;
	private String classification;
	private String subClassification;
	private String description;
	private String processId;
	private String taksId;
	private String taskStatus;
	private String rootCause;
	private String rootCauseDesc;
	
	public String getTaksId() {
		return taksId;
	}

	public void setTaksId(String taksId) {
		this.taksId = taksId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getRootCauseDesc() {
		return rootCauseDesc;
	}

	public void setRootCauseDesc(String rootCauseDesc) {
		this.rootCauseDesc = rootCauseDesc;
	}

	@Override
	public String toString() {
		return "TaskNoteDto [wellName=" + wellName + ", classification=" + classification + ", subClassification="
				+ subClassification + ", description=" + description + ", processId=" + processId + ", taksId=" + taksId
				+ ", taskStatus=" + taskStatus + ", rootCause=" + rootCause + ", rootCauseDesc=" + rootCauseDesc + "]";
	}

	

	

	
}
