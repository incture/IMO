package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="DT_FLARE_CODE")
public class DowntimeFlareCodeDo implements Serializable,BaseDo {
	@Id
	@Column(name="FLARE_CODE",length=100)
private String flareCode;
	@Column(name="FLARE_CODE_DESCRIPTION",length=100)
private String flareCodeDescription;
	@Column(name="DEPENDENT_VALUE",length=100)
private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=100)
	private String activeFlag;

	public String getFlareCode() {
		return flareCode;
	}
	public void setFlareCode(String flareCode) {
		this.flareCode = flareCode;
	}
	public String getFlareCodeDescription() {
		return flareCodeDescription;
	}
	public void setFlareCodeDescription(String flareCodeDescription) {
		this.flareCodeDescription = flareCodeDescription;
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
	@Override
	public Object getPrimaryKey() {
		return flareCode;
	}
	
}
