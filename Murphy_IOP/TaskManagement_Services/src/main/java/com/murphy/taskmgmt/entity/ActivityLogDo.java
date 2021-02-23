package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EI_ACTIVITY_LOG")
public class ActivityLogDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "FORM_ID")
	private String formId;
	
	@Column(name = "PERM_ISSUER_NAME")
	private String permIssueName;
	
	@Column(name = "PERM_ISSUER_LOGIN_NAME")
	private String permIssueLoginName;
	
	@Column(name = "IS_APPROVED")
	private String isApproved;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "PERM_ISSUE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date permIssueTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPermIssueName() {
		return permIssueName;
	}

	public void setPermIssueName(String permIssueName) {
		this.permIssueName = permIssueName;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public Date getPermIssueTime() {
		return permIssueTime;
	}

	public void setPermIssueTime(Date permIssueTime) {
		this.permIssueTime = permIssueTime;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPermIssueLoginName() {
		return permIssueLoginName;
	}

	public void setPermIssueLoginName(String permIssueLoginName) {
		this.permIssueLoginName = permIssueLoginName;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}
}
