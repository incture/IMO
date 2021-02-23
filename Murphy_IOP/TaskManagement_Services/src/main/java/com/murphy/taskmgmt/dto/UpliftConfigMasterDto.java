package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class UpliftConfigMasterDto extends BaseDto{

	private Integer weatherConditionCode;
	private String description;
	private Double upliftPercentage;

	public Integer getWeatherConditionCode() {
		return weatherConditionCode;
	}

	public void setWeatherConditionCode(Integer weatherConditionCode) {
		this.weatherConditionCode = weatherConditionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getUpliftPercentage() {
		return upliftPercentage;
	}

	public void setUpliftPercentage(Double upliftPercentage) {
		this.upliftPercentage = upliftPercentage;
	}

	@Override
	public String toString() {
		return "UpliftConfigMasterDto [weatherConditionCode=" + weatherConditionCode + ", description=" + description
				+ ", upliftPercentage=" + upliftPercentage + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		
	}

}
