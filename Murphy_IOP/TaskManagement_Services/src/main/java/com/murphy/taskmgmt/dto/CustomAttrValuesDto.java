package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CustomAttrValuesDto extends BaseDto {


	private String valueId;	
	private String clItemId;
	private String value;
	private String dependentValue;

	public String getDependentValue() {
		return dependentValue;
	}

	public void setDependentValue(String dependentValue) {
		this.dependentValue = dependentValue;
	}

	
	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getClItemId() {
		return clItemId;
	}

	public void setClItemId(String clItemId) {
		this.clItemId = clItemId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	@Override
	public String toString() {
		return "CustomAttrValuesDto [valueId=" + valueId + ", clItemId=" + clItemId + ", value=" + value
				+ ", dependentValue=" + dependentValue + "]";
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
