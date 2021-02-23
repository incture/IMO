package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class StopTimeDto extends BaseDto  {

	private String id;
	private String classification;
	private String subClassification;
	private int proA;
	private int proB;
	private int obx;
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

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

	@Override
	public String toString() {
		return "StopTimeDto [id=" + id + ", classification=" + classification + ", subClassification="
				+ subClassification + ", proA=" + proA + ", proB=" + proB + ", obx=" + obx + ", sse=" + sse + "]";
	}
	
}
