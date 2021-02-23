package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nivedha Chandhirika
 * This table holds drop down values of Frac Scenario
 *
 */
@Entity
@Table(name = "FRAC_SCENARIO_LOOK_UP")
public class FracScenarioLookUpDo implements Serializable, BaseDo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCENARIO",length = 200)
	private String scenario;
	
	@Column(name="DEPENDENT_VALUE",length=200)
	private String dependentValue;
	
	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;
	
	
	
	
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getDependentValue() {
		return dependentValue;
	}

	public void setDependentValue(String dependentValue) {
		this.dependentValue = dependentValue;
	}

	

	@Override
	public Object getPrimaryKey() {
		return scenario;
	}

}
