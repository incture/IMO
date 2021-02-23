package com.murphy.taskmgmt.dto;

import java.sql.Clob;
import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class EIContractorDto extends BaseDto {
	
	private String id; 
	private String contractorName; //name
	private String emailId; //emailId
	private String contractorPerformingWork; //contractorPerformWork
	private String signatureContent;	// base64
//	private String signatureContent;	// base64
	private Date updatedAt;
	private String formId;
	private Date createdAt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContractorPerformingWork() {
		return contractorPerformingWork;
	}
	public void setContractorPerformingWork(String contractorPerformingWork) {
		this.contractorPerformingWork = contractorPerformingWork;
	}
	public String getSignatureContent() {
		return signatureContent;
	}
	public void setSignatureContent(String signatureContent) {
		this.signatureContent = signatureContent;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
	@Override
	public String toString() {
		return "EIContractorDto [id=" + id + ", contractorName=" + contractorName + ", emailId=" + emailId
				+ ", contractorPerformingWork=" + contractorPerformingWork + ", signatureContent=" + signatureContent
				+ ", updatedAt=" + updatedAt + ", formId=" + formId + ", createdAt=" + createdAt + "]";
	}
	
}
