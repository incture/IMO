package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nivedha Chandhirika
 * This table holds drop down values of Frac well status
 *
 */
@Entity
@Table(name = "FRAC_WELL_STATUS")
public class FracWellStatusDo implements Serializable, BaseDo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WELL_STATUS",length = 50)
	private String wellStatus;

	@Column(name = "DEPENDENT_VALUE",length=200)
	private String dependentValue;

	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;
	
	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
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
		return wellStatus;
	}

}
