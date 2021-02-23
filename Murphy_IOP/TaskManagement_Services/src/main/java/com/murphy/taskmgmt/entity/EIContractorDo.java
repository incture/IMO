package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "EI_CONTRACTOR_DATA")
public class EIContractorDo implements BaseDo, Serializable {
	private static final long serialVersionUID = 119130939L;
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	@Column(name = "FORM_ID", length = 40)
	private String formId;
	@Column(name = "CONTRACTOR_NAME", length = 100)
	private String contractorName;
	@Column(name = "EMAIL_ID", length = 100)
	private String emailId;
	@Column(name = "CONTRACTOR_PERFORM_WORK", length = 100)
	private String contractorPerformingWork;
	@Column(name = "CREATED_AT")
	private Date createdAt;
	@Column(name = "UPDATED_AT")
	private Date updatedAt;
	@Lob
	@Column(name = "SIGNATURE_CONTENT")
	private String signatureContent;	// base64
//	private String signatureContent;	// base64
	
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
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@Override
	public Object getPrimaryKey() {
		return id;
	}
	
}
