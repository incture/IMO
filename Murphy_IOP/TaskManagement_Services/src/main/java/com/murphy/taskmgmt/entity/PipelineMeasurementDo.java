package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PIPELINE_MEASUREMENT")
public class PipelineMeasurementDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
	@Column(name = "EQUIPMENT_ID", length = 20)
	private String equipmentId;

	@Column(name = "LINE_NAME", length = 100)
	private String lineName;

	@Column(name = "FLOC_ID", length = 50)
	private String flocId;

	@Column(name = "LENGTH")
	private Integer length;

	@Column(name = "DIAMETER")
	private Integer diameter;

	@Column(name = "START_LOCATION", length = 50)
	private String startLocation;

	@Column(name = "END_LOCATION", length = 50)
	private String endLocation;
	
	@Column(name="FREQUENCY")
	private Integer frequency;
	
	
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

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
