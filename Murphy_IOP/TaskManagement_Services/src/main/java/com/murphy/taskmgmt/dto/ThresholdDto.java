package com.murphy.taskmgmt.dto;

public class ThresholdDto {
	
	private double maximumValue;	
	private double minimumValue;	
	private String condition;
	private String tier;
	
	public double getMaximumValue() {
		return maximumValue;
	}
	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;
	}
	public double getMinimumValue() {
		return minimumValue;
	}
	public void setMinimumValue(double minimumValue) {
		this.minimumValue = minimumValue;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "ThresholdDto [maximumValue=" + maximumValue + ", minimumValue=" + minimumValue + ", condition="
				+ condition + "]";
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}

}
