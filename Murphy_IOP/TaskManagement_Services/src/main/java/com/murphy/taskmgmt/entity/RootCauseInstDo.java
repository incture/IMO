package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_ROOTCAUSE_INSTS")
public class RootCauseInstDo implements BaseDo {

	@Id
	@Column(name = "UNIQUE_ID", length = 32)
	private String uniqueId = UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "TASK_ID", length = 32)
	private String taskId;

	@Column(name = "ROOT_CAUSE", length = 100)
	private String rootCause;
	
	@Column(name = "ACTION", length = 100)
	private String action;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	@Column(name = "SUB_CLASSIFCATION", length = 200)
	private String subClassification;


	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return uniqueId;
	}

	@Override
	public String toString() {
		return "RootCauseInstDo [uniqueId=" + uniqueId + ", taskId=" + taskId + ", rootCause=" + rootCause + ", action="
				+ action + ", createdAt=" + createdAt + ", description=" + description + ", subClassification="
				+ subClassification + "]";
	}
	
	

	
	
	
}
