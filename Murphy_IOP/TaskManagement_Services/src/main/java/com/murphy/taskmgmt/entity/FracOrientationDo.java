package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nivedha Chandhirika
 * This table holds drop down values of Frac Orientation
 *
 */
@Entity
@Table(name = "FRAC_ORIENTATION")
public class FracOrientationDo implements Serializable, BaseDo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ORIENTATION",length=100)
	private String orientation;

	@Column(name = "DEPENDENT_VALUE",length=100)
	private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
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
		return orientation;
	}

}
