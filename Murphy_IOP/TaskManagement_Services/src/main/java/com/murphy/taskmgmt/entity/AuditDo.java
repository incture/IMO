package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_AUDIT_TRAIL")
public class AuditDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUDIT_ID", length = 32)
	private String auditId=UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "TASK_ID", length = 32)
	private String taskId;
	
	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "ACTION", length = 32)
	private String action;
	
	@Column(name = "ACTION_BY")
	private String actionBy;
	

	


	@Override
	public String toString() {
		return "AuditDo [auditId=" + auditId + ", taskId=" + taskId + ", createdAt=" + createdAt + ", action=" + action
				+ ", actionBy=" + actionBy + "]";
	}





	@Override
	public Object getPrimaryKey() {
		return auditId;
	}





	public String getAuditId() {
		return auditId;
	}





	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}





	public String getTaskId() {
		return taskId;
	}





	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}





	public Date getCreatedAt() {
		return createdAt;
	}





	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}





	public String getAction() {
		return action;
	}





	public void setAction(String action) {
		this.action = action;
	}





	public String getActionBy() {
		return actionBy;
	}





	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

}
