package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class TaskTemplatesDto extends BaseDto {

	private String taskTemplateId;
	private String processTemplateId;
	private String description;
	private String subject;
	private String name;
	private String priority;
	private String sla;
	private String taskType;
	private String ownerGroup;
	private String ownerGroupId;
	private String url;
	//	private Boolean shouldWait;
	//	private String ifAccepts;
	//	private String ifRejects;

	private List<CustomAttrInstancesDto> clInstanceList;
	private List<CustomAttrTemplateDto> attrList;

	/*	private List<CheckListTemplateDto> preProcCheckList;
	private List<CheckListTemplateDto> postProcCheckList;*/

	/*	public List<CheckListTemplateDto> getPreProcCheckList() {
		return preProcCheckList;
	}

	public void setPreProcCheckList(List<CheckListTemplateDto> preProcCheckList) {
		this.preProcCheckList = preProcCheckList;
	}

	public List<CheckListTemplateDto> getPostProcCheckList() {
		return postProcCheckList;
	}

	public void setPostProcCheckList(List<CheckListTemplateDto> postProcCheckList) {
		this.postProcCheckList = postProcCheckList;
	}*/

	public List<CustomAttrTemplateDto> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<CustomAttrTemplateDto> attrList) {
		this.attrList = attrList;
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
	//	public Boolean getShouldWait() {
	//		return shouldWait;
	//	}
	//
	//	public void setShouldWait(Boolean shouldWait) {
	//		this.shouldWait = shouldWait;
	//	}


	@Override
	public String toString() {
		return "TaskTemplatesDto [taskTemplateId=" + taskTemplateId + ", processTemplateId=" + processTemplateId
				+ ", description=" + description + ", subject=" + subject + ", name=" + name + ", priority=" + priority
				+ ", sla=" + sla + ", taskType=" + taskType + ", ownerGroup=" + ownerGroup + ", ownerGroupId="
				+ ownerGroupId + ", url=" + url + ", clInstanceList=" + clInstanceList + ", attrList=" + attrList + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	public String getOwnerGroupId() {
		return ownerGroupId;
	}

	public void setOwnerGroupId(String ownerGroupId) {
		this.ownerGroupId = ownerGroupId;
	}

	public List<CustomAttrInstancesDto> getClInstanceList() {
		return clInstanceList;
	}

	public void setClInstanceList(List<CustomAttrInstancesDto> clInstanceList) {
		this.clInstanceList = clInstanceList;
	}




}
