package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;
public class ProcessTemplateDto extends BaseDto {
	
	private String processTemplateId;
	private String processName;
	private String createdBy;
	private String createdByDisplay;
	private Date createdAt;
	private String sla;
	private String tempType;
	private Date lastUpdated;
	private List<TaskTemplatesDto> taskTemplates;
	
	
	
	public List<TaskTemplatesDto> getTaskTemplates() {
		return taskTemplates;
	}


	public void setTaskTemplates(List<TaskTemplatesDto> taskTemplates) {
		this.taskTemplates = taskTemplates;
	}


	public String getProcessTemplateId() {
		return processTemplateId;
	}


	public void setProcessTemplateId(String processTemplateId) {
		this.processTemplateId = processTemplateId;
	}

	public String getProcessName() {
		return processName;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedByDisplay() {
		return createdByDisplay;
	}

	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getSla() {
		return sla;
	}


	public void setSla(String sla) {
		this.sla = sla;
	}


	public String getTempType() {
		return tempType;
	}


	public void setTempType(String tempType) {
		this.tempType = tempType;
	}


	public Date getLastUpdated() {
		return lastUpdated;
	}


	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "ProcessTemplateDto [processTemplateId=" + processTemplateId + ", processName=" + processName
				+ ", createdBy=" + createdBy + ", createdByDisplay=" + createdByDisplay + ", createdAt=" + createdAt
				+ ", sla=" + sla + ", tempType=" + tempType + ", lastUpdated=" + lastUpdated + ", taskTemplates="
				+ taskTemplates + "]";
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


	
}
