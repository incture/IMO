package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DT_WELL_PARENT_CODE")
public class DowntimeWellParentCodeDo implements Serializable,BaseDo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARENT_CODE",length=100)
	private String parentCode;
	
	@Column(name="PARENT_CODE_DESCRIPTION",length=200)
	private String parentCodeDescription;
	
	@Column(name="DEPENDENT_VALUE",length=200)
	private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;

	@Override
	public Object getPrimaryKey() {
		return parentCode; 
	
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentCodeDescription() {
		return parentCodeDescription;
	}

	public void setParentCodeDescription(String parentCodeDescription) {
		this.parentCodeDescription = parentCodeDescription;
	}

	public String getDependentValue() {
		return dependentValue;
	}

	public void setDependentValue(String dependentValue) {
		this.dependentValue = dependentValue;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	

}
