package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: TaskTemplatesDo
 *
 */
@Entity
@Table(name = "TM_TASK_TEMPS")
public class TaskTemplatesDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public TaskTemplatesDo() {
		super();
	}

	private static final long serialVersionUID = -7341365853980611944L;

	@Id
	@Column(name = "TASK_TEMP_ID", length = 32)
	private String taskTemplateId ;

	@Column(name = "PROC_TEMP_ID", length = 100)
	private String processTemplateId;

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	@Column(name = "SUBJECT", length = 250)
	private String subject;

	@Column(name = "NAME", length = 100)
	private String name;

	@Column(name = "PRIORITY", length = 20)
	private String priority;

	@Column(name = "SLA", length = 20)
	private String sla;

	@Column(name = "TASK_TYPE", length = 50)
	private String taskType;

	@Column(name = "TASK_OWNER_GRP", length = 100)
	private String ownerGroup;

	//	@Column(name = "SHOULD_WAIT", length = 10)
	//	private Boolean shouldWait;

	//	@Column(name = "IF_ACCEPTS", length = 100)
	//	private String ifAccepts;
	//
	//	@Column(name = "IF_REJECTS", length = 100)
	//	private String ifRejects;


	@Column(name = "URL", length = 200)
	private String url;

	@Column(name = "GROUP_ID", length = 32)
	private String ownerGroupId;

	@Column(name = "PREV_TASK", length = 32)
	private String prevTask;


	public String getOwnerGroupId() {
		return ownerGroupId;
	}

	public void setOwnerGroupId(String ownerGroupId) {
		this.ownerGroupId = ownerGroupId;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	public void setTaskTemplateId(String taskTemplateId) {
		this.taskTemplateId = taskTemplateId;
	}

	public String getProcessTemplateId() {
		return processTemplateId;
	}

	public void setProcessTemplateId(String processTemplateId) {
		this.processTemplateId = processTemplateId;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getOwnerGroup() {
		return ownerGroup;
	}

	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}


	//	public Boolean getShouldWait() {
	//		return shouldWait;
	//	}
	//
	//	public void setShouldWait(Boolean shouldWait) {
	//		this.shouldWait = shouldWait;
	//	}


	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Object getPrimaryKey() {
		return taskTemplateId;
	}

	//	public String getIfAccepts() {
	//		return ifAccepts;
	//	}
	//
	//	public void setIfAccepts(String ifAccepts) {
	//		this.ifAccepts = ifAccepts;
	//	}
	//
	//	public String getIfRejects() {
	//		return ifRejects;
	//	}
	//
	//	public void setIfRejects(String ifRejects) {
	//		this.ifRejects = ifRejects;
	//	}

	@Override
	public String toString() {
		return "TaskTemplatesDo [taskTemplateId=" + taskTemplateId + ", processTemplateId=" + processTemplateId
				+ ", description=" + description + ", subject=" + subject + ", name=" + name + ", priority=" + priority
				+ ", sla=" + sla + ", taskType=" + taskType + ", ownerGroup=" + ownerGroup + ", shouldWait="
				//	+ shouldWait + " ", ifAccepts=" + ifAccepts + ", ifRejects=" + ifRejects + 
				+",url=" + url+ ", ownerGroupId=" + ownerGroupId + "]";
	}



}
