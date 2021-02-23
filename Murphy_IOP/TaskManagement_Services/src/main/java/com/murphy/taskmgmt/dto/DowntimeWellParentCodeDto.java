package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeWellParentCodeDto extends BaseDto {
	private String parentCode;
	private String parentCodeDescription;

	private String dependentValue;

	private String activeFlag;
	
	private String countryCode;
	

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentCodeDescription() {
		return parentCodeDescription;
	}

	public void setParentCodeDescription(String parentCodeDescription) {
		this.parentCodeDescription = parentCodeDescription;
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
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "DowntimeWellParentCodeDto [parentCode=" + parentCode + ", parentCodeDescription="
				+ parentCodeDescription + ", dependentValue=" + dependentValue + ", activeFlag=" + activeFlag
				+ ", countryCode=" + countryCode + "]";
	}

}
