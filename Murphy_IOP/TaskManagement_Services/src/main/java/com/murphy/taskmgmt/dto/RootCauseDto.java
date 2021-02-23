package com.murphy.taskmgmt.dto;

import java.util.List;

public class RootCauseDto {

	private List<String> rootCauseList;
	private List<List<String>> subClassList;
	private String rootCause;
	private String Description;
	private String status;
	private String subClassification;
	private String classification;

	
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public List<List<String>> getSubClassList() {
		return subClassList;
	}

	public void setSubClassList(List<List<String>> subClassList) {
		this.subClassList = subClassList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public List<String> getRootCauseList() {
		return rootCauseList;
	}

	public void setRootCauseList(List<String> rootCauseList) {
		this.rootCauseList = rootCauseList;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return "RootCauseDto [rootCauseList=" + rootCauseList + ", subClassList=" + subClassList + ", rootCause="
				+ rootCause + ", Description=" + Description + ", status=" + status + ", subClassification="
				+ subClassification + "]";
	}

}
