package com.murphy.taskmgmt.dto;

public class BypassRiskLevelDto {
	
	private String riskLevel;
	private String activeFlag;
	
	
	/**
	 * @return the riskLevel
	 */
	public String getRiskLevel() {
		return riskLevel;
	}


	/**
	 * @param riskLevel the riskLevel to set
	 */
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}


	/**
	 * @return the activeFlag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}


	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}


	@Override
	public String toString() {
		return "BypassRiskLevelDto [riskLevel=" + riskLevel + ", activeFlag=" + activeFlag + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	

}
