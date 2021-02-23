package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_TASK_STOP_TIME_BY_ROLE")
public class StopTimeDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 40)
	private String id;

	@Column(name = "CLASSIFICATION")
	private String classification;

	@Column(name = "SUB_CLASSIFICATION")
	private String subClassification;

	@Column(name = "PRO_A")
	private int proA;

	@Column(name = "PRO_B")
	private int proB;

	@Column(name = "OBX")
	private int obx;

	@Column(name = "SSE")
	private int sse;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public int getProA() {
		return proA;
	}

	public void setProA(int proA) {
		this.proA = proA;
	}

	public int getProB() {
		return proB;
	}

	public void setProB(int proB) {
		this.proB = proB;
	}

	public int getObx() {
		return obx;
	}

	public void setObx(int obx) {
		this.obx = obx;
	}

	public int getSse() {
		return sse;
	}

	public void setSse(int sse) {
		this.sse = sse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
