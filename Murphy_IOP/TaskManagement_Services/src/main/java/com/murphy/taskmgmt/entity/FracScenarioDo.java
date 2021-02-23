package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FRAC_SCENARIO")
public class FracScenarioDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766420234395997999L;
	@Id
	@Column(name = "SCENARIO", length = 200)
	private String scenario;
	
	@Column(name = "WELL_STATUS", length = 50)
	private String wellStatus;

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getWellStatus() {
		return wellStatus;
	}

	public void setWellStatus(String wellStatus) {
		this.wellStatus = wellStatus;
	}

	@Override
	public String toString() {
		return "FracScenarioDo [scenario=" + scenario + ", wellStatus=" + wellStatus + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
