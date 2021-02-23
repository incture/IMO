package com.murphy.taskmgmt.dto;

import java.util.List;

import com.murphy.geotab.NearestUserDto;

public class OperatorsAvailabilityDto {
	
	private String classification;
	private String subClassifiaction;
	private List<NearestUserDto> nearestUserDto;
	@Override
	public String toString() {
		return "OperatorsAvailabilityDto [classification=" + classification + ", subClassifiaction=" + subClassifiaction
				+ ", nearestUserDto=" + nearestUserDto + "]";
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubClassifiaction() {
		return subClassifiaction;
	}
	public void setSubClassifiaction(String subClassifiaction) {
		this.subClassifiaction = subClassifiaction;
	}
	public List<NearestUserDto> getNearestUserDto() {
		return nearestUserDto;
	}
	public void setNearestUserDto(List<NearestUserDto> nearestUserDto) {
		this.nearestUserDto = nearestUserDto;
	}

}
