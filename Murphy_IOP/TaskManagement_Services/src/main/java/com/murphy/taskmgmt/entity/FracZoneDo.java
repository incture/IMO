package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nivedha Chandhirika This table holds drop down values of Frac Zone
 *
 */
@Entity
@Table(name = "FRAC_ZONE")
public class FracZoneDo implements Serializable, BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ZONE",length=100)
	private String zone;

	@Column(name = "DEPENDENT_VALUE",length=100)
	private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
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
		return zone;
	}

}
