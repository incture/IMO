package com.murphy.taskmgmt.dto;

import java.util.Date;

public class AuditReportDto {
	
	private Long requestId;	
	private String ndTaskId;
	private String description;
	private String location;
	private String classification;
	private String subClassification;
	private String status;
	private String createdBy;
	private Date createdAt;
	private String createdAtDisplay;
	private Date completedAt;
	private String completedAtDisplay;
	private String completedBy;
	private Date resolvedAt;
	private String resolvedAtDisplay;
	private Date acknowledgedAt;
	private String acknowledgedAtDisplay;
	private String assignedTo ;
	
	
	
	
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	public Date getResolvedAt() {
		return resolvedAt;
	}
	public void setResolvedAt(Date resolvedAt) {
		this.resolvedAt = resolvedAt;
	}
	public Date getAcknowledgedAt() {
		return acknowledgedAt;
	}
	public void setAcknowledgedAt(Date acknowledgedAt) {
		this.acknowledgedAt = acknowledgedAt;
	}


	@Override
	public String toString() {
		return "AuditReportDto [requestId=" + requestId + ", ndTaskId=" + ndTaskId + ", description=" + description
				+ ", location=" + location + ", classification=" + classification + ", subClassification="
				+ subClassification + ", status=" + status + ", createdBy=" + createdBy + ", createdAt=" + createdAt
				+ ", createdAtDisplay=" + createdAtDisplay + ", completedAt=" + completedAt + ", completedAtDisplay="
				+ completedAtDisplay + ", completedBy=" + completedBy + ", resolvedAt=" + resolvedAt
				+ ", resolvedAtDisplay=" + resolvedAtDisplay + ", acknowledgedAt=" + acknowledgedAt
				+ ", acknowledgedAtDisplay=" + acknowledgedAtDisplay + ", assignedTo=" + assignedTo + "]";
	}
	public String getNdTaskId() {
		return ndTaskId;
	}
	public void setNdTaskId(String ndTaskId) {
		this.ndTaskId = ndTaskId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedAtDisplay() {
		return createdAtDisplay;
	}
	public void setCreatedAtDisplay(String createdAtDisplay) {
		this.createdAtDisplay = createdAtDisplay;
	}
	public String getCompletedAtDisplay() {
		return completedAtDisplay;
	}
	public void setCompletedAtDisplay(String completedAtDisplay) {
		this.completedAtDisplay = completedAtDisplay;
	}
	public String getCompletedBy() {
		return completedBy;
	}
	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}
	public String getResolvedAtDisplay() {
		return resolvedAtDisplay;
	}
	public void setResolvedAtDisplay(String resolvedAtDisplay) {
		this.resolvedAtDisplay = resolvedAtDisplay;
	}
	public String getAcknowledgedAtDisplay() {
		return acknowledgedAtDisplay;
	}
	public void setAcknowledgedAtDisplay(String acknowledgedAtDisplay) {
		this.acknowledgedAtDisplay = acknowledgedAtDisplay;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}


}
