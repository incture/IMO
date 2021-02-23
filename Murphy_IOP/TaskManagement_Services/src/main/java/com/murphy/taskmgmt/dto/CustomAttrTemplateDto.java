package com.murphy.taskmgmt.dto;

import java.util.Date;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CustomAttrTemplateDto extends BaseDto {


	private String clItemId ;
	private String taskTempId;
	private String label;
	private String dataType;
	private Boolean isMandatory;
	private Boolean isDefault;
	private  Integer maxLength;
	private Integer seqNumber;
	private String shortDesc;
	private Boolean isEditable;
	private List<CustomAttrValuesDto> valueDtos;
	private List<String> values;
	private String labelValue;
	private Boolean isUpdatable;
	private String dependentOn;
	private Date dateValue;
	private Boolean isEnabled;
	private String taskId;
	
/*	private String serviceUrl;
	private String dataPath;*/
	
	
	
	
	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Boolean getIsUpdatable() {
		return isUpdatable;
	}


	public void setIsUpdatable(Boolean isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	
	
	public List<CustomAttrValuesDto> getValueDtos() {
		return valueDtos;
	}


	public void setValueDtos(List<CustomAttrValuesDto> valueDtos) {
		this.valueDtos = valueDtos;
	}


	public List<String> getValues() {
		return values;
	}


	public void setValues(List<String> values) {
		this.values = values;
	}


	public String getClItemId() {
		return clItemId;
	}


	public void setClItemId(String clItemId) {
		this.clItemId = clItemId;
	}


	public String getTaskTempId() {
		return taskTempId;
	}


	public void setTaskTempId(String taskTempId) {
		this.taskTempId = taskTempId;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public Boolean getIsMandatory() {
		return isMandatory;
	}


	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}


	public Integer getMaxLength() {
		return maxLength;
	}


	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}


	public Boolean getIsDefault() {
		return isDefault;
	}


	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getSeqNumber() {
		return seqNumber;
	}


	public void setSeqNumber(Integer seqNumber) {
		this.seqNumber = seqNumber;
	}


	public String getShortDesc() {
		return shortDesc;
	}


	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}


	public Boolean getIsEditable() {
		return isEditable;
	}


	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}


/*	public String getServiceUrl() {
		return serviceUrl;
	}


	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	public String getDataPath() {
		return dataPath;
	}


	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}*/

	public Boolean getIsEnabled() {
		return isEnabled;
	}


	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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


	public String getLabelValue() {
		return labelValue;
	}


	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}


	public String getDependentOn() {
		return dependentOn;
	}


	public void setDependentOn(String dependentOn) {
		this.dependentOn = dependentOn;
	}


	public Date getDateValue() {
		return dateValue;
	}


	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	@Override
	public String toString() {
		return "CustomAttrTemplateDto [clItemId=" + clItemId + ", taskTempId=" + taskTempId + ", label=" + label
				+ ", dataType=" + dataType + ", isMandatory=" + isMandatory + ", isDefault=" + isDefault
				+ ", maxLength=" + maxLength + ", seqNumber=" + seqNumber + ", shortDesc=" + shortDesc + ", isEditable="
				+ isEditable + ", valueDtos=" + valueDtos + ", values=" + values + ", labelValue=" + labelValue
				+ ", isUpdatable=" + isUpdatable + ", dependentOn=" + dependentOn + ", isEnabled=" + isEnabled
				+ ", taskId=" + taskId + "]";
	}

}
