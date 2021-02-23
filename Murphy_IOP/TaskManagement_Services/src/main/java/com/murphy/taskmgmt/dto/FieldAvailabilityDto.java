package com.murphy.taskmgmt.dto;

import java.util.List;

public class FieldAvailabilityDto {
private String field;
private String fieldAvailabilityPercentage;
private double fieldAvailablePercent;
private  List<FieldOperatorAvailabilityDto> OperatorAvailabilityList;
private int noOfOperators;
public double getFieldAvailablePercent() {
	return fieldAvailablePercent;
}
public void setFieldAvailablePercent(double fieldAvailablePercent) {
	this.fieldAvailablePercent = fieldAvailablePercent;
}

public String getField() {
	return field;
}
public void setField(String field) {
	this.field = field;
}
public int getNoOfOperators() {
	return noOfOperators;
}
public void setNoOfOperators(int noOfOperators) {
	this.noOfOperators = noOfOperators;
}
public String getFieldAvailabilityPercentage() {
	return fieldAvailabilityPercentage;
}
public void setFieldAvailabilityPercentage(String fieldAvailabilityPercentage) {
	this.fieldAvailabilityPercentage = fieldAvailabilityPercentage;
}
public List<FieldOperatorAvailabilityDto> getOperatorAvailabilityList() {
	return OperatorAvailabilityList;
}
public void setOperatorAvailabilityList(List<FieldOperatorAvailabilityDto> operatorAvailabilityList) {
	OperatorAvailabilityList = operatorAvailabilityList;
}



}
