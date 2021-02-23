package com.murphy.integration.dto;

import java.util.Date;

public class EnersightProveMonthlyDto {

	private double avgActualBoed;
	private double avgForecastBoed;
	private double avgVarToForecastBoed;
	private double avgPerVarToForecastBoed;
	private double avgOil;
	private double avgWater;
	private double avgGas;
	private String well;
	private String lastProdDate;
	private Date lastProdDateField;
	private Integer maxThresholdValue;
	private String muwiId;
	private String locationCode;
	private boolean investigationInProgress;
	private boolean inquiryInProgress;

	public double getAvgActualBoed() {
		return avgActualBoed;
	}

	public void setAvgActualBoed(double avgActualBoed) {
		this.avgActualBoed = avgActualBoed;
	}

	public double getAvgForecastBoed() {
		return avgForecastBoed;
	}

	public void setAvgForecastBoed(double avgForecastBoed) {
		this.avgForecastBoed = avgForecastBoed;
	}

	public double getAvgVarToForecastBoed() {
		return avgVarToForecastBoed;
	}

	public void setAvgVarToForecastBoed(double avgVarToForecastBoed) {
		this.avgVarToForecastBoed = avgVarToForecastBoed;
	}

	public double getAvgPerVarToForecastBoed() {
		return avgPerVarToForecastBoed;
	}

	public void setAvgPerVarToForecastBoed(double avgPerVarToForecastBoed) {
		this.avgPerVarToForecastBoed = avgPerVarToForecastBoed;
	}

	public double getAvgOil() {
		return avgOil;
	}

	public void setAvgOil(double avgOil) {
		this.avgOil = avgOil;
	}

	public double getAvgWater() {
		return avgWater;
	}

	public void setAvgWater(double avgWater) {
		this.avgWater = avgWater;
	}

	public double getAvgGas() {
		return avgGas;
	}

	public void setAvgGas(double avgGas) {
		this.avgGas = avgGas;
	}

	public String getWell() {
		return well;
	}

	public void setWell(String well) {
		this.well = well;
	}

	public String getLastProdDate() {
		return lastProdDate;
	}

	public void setLastProdDate(String lastProdDate) {
		this.lastProdDate = lastProdDate;
	}

	public Integer getMaxThresholdValue() {
		return maxThresholdValue;
	}

	public void setMaxThresholdValue(Integer maxThresholdValue) {
		this.maxThresholdValue = maxThresholdValue;
	}

	public String getMuwiId() {
		return muwiId;
	}

	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public boolean isInvestigationInProgress() {
		return investigationInProgress;
	}

	public void setInvestigationInProgress(boolean investigationInProgress) {
		this.investigationInProgress = investigationInProgress;
	}

	public Date getLastProdDateField() {
		return lastProdDateField;
	}

	public void setLastProdDateField(Date lastProdDateField) {
		this.lastProdDateField = lastProdDateField;
	}

	public boolean isInquiryInProgress() {
		return inquiryInProgress;
	}

	public void setInquiryInProgress(boolean inquiryInProgress) {
		this.inquiryInProgress = inquiryInProgress;
	}

	@Override
	public String toString() {
		return "EnersightProveMonthlyDto [avgActualBoed=" + avgActualBoed + ", avgForecastBoed=" + avgForecastBoed
				+ ", avgVarToForecastBoed=" + avgVarToForecastBoed + ", avgPerVarToForecastBoed="
				+ avgPerVarToForecastBoed + ", avgOil=" + avgOil + ", avgWater=" + avgWater + ", avgGas=" + avgGas
				+ ", well=" + well + ", lastProdDate=" + lastProdDate + ", lastProdDateField=" + lastProdDateField
				+ ", maxThresholdValue=" + maxThresholdValue + ", muwiId=" + muwiId + ", locationCode=" + locationCode
				+ ", investigationInProgress=" + investigationInProgress + ", inquiryInProgress=" + inquiryInProgress
				+ "]";
	}
}
