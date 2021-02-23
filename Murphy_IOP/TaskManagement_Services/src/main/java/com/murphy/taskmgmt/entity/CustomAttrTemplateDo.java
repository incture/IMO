package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: CustomAttrTemplateDo
 *
 */
@Entity
@Table(name = "TM_ATTR_TEMP")
public class CustomAttrTemplateDo implements BaseDo, Serializable {

	public CustomAttrTemplateDo() {
		super();
	}

	private static final long serialVersionUID = -7341365853980611944L;

	@Id
	@Column(name = "ATTR_ID", length = 32)
	private String clItemId ;

	@Column(name = "TASK_TEMP_ID", length = 32)
	private String taskTempId;

	@Column(name = "LABEL", length = 100)
	private String label;
	
	@Column(name = "DATA_TYPE", length = 50)
	private String dataType;
	
	@Column(name = "IS_MAND")
	private Boolean isMandatory;
	
	@Column(name = "IS_DEFAULT")
	private Boolean isDefault;

	@Column(name = "MAX_LENGTH")
	private  Integer maxLength;
	
	@Column(name = "SEQ_NO")
	private Integer seqNumber;
	
	@Column(name = "SHORT_DESC" ,length = 200)
	private String shortDesc;
	
	@Column(name = "IS_EDITABLE")
	private Boolean isEditable;
	
	/*@Column(name = "SERVICE_URL" , length = 100)
	private String serviceUrl;
	
	@Column(name = "DATA_PATH" , length = 100)
	private String dataPath;*/
	
	@Column(name = "IS_UPDATABLE")
	private Boolean isUpdatable;
	
	@Column(name = "DEPENDENT_ON" , length = 32)
	private String dependentOn;
	
	
	
	
	public Boolean getIsUpdatable() {
		return isUpdatable;
	}


	public void setIsUpdatable(Boolean isUpdatable) {
		this.isUpdatable = isUpdatable;
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


	public String getDependentOn() {
		return dependentOn;
	}


	public void setDependentOn(String dependentOn) {
		this.dependentOn = dependentOn;
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


	@Override
	public Object getPrimaryKey() {
		return clItemId;
	}


	@Override
	public String toString() {
		return "CustomAttrTemplateDo [clItemId=" + clItemId + ", taskTempId=" + taskTempId + ", label=" + label
				+ ", dataType=" + dataType + ", isMandatory=" + isMandatory + ", isDefault=" + isDefault
				+ ", maxLength=" + maxLength + ", seqNumber=" + seqNumber + ", shortDesc=" + shortDesc + ", isEditable="
				+ isEditable + ", isUpdatable=" + isUpdatable + ", dependentOn=" + dependentOn + "]";
	}




}
