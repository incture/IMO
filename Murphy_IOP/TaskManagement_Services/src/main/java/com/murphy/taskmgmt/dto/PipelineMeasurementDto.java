package com.murphy.taskmgmt.dto;

import java.io.Serializable;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class PipelineMeasurementDto extends BaseDto implements  Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String equipmentId;


	private String lineName;

	
	private String flocId;


	private Integer length;

	
	private Integer diameter;

	private String startLocation;

	
	private String endLocation;
	
	
	private Integer frequency;
	
	
	
	
	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getFlocId() {
		return flocId;
	}

	public void setFlocId(String flocId) {
		this.flocId = flocId;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getDiameter() {
		return diameter;
	}

	public void setDiameter(Integer diameter) {
		this.diameter = diameter;
	}

	

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
