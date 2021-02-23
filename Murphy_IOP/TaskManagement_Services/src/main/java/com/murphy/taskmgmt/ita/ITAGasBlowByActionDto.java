package com.murphy.taskmgmt.ita;

import com.fasterxml.jackson.databind.JsonNode;

public class ITAGasBlowByActionDto implements RuleOutputDto {
	
	
	
	@Override
	public String toString() {
		return "ITAGasBlowByActionDto [taskClassification=" + taskClassification + ", taskSubClassification="
				+ taskSubClassification + ", taskToBeCreated=" + taskToBeCreated + ", dailyOilValue=" + dailyOilValue
				+ ", comparisonRatio=" + comparisonRatio + ", oilMeterMerrickId=" + oilMeterMerrickId
				+ ", gasMeterMerrickId=" + gasMeterMerrickId + ", meterName=" + meterName + "]";
	}

	private String taskClassification;
	private String taskSubClassification;
	private String taskToBeCreated;
	private Double dailyOilValue;
	private Double comparisonRatio;
	private String oilMeterMerrickId;
	private String gasMeterMerrickId;
	private String meterName;
	
	
	public String getTaskClassification() {
		return taskClassification;
	}


	public void setTaskClassification(String taskClassification) {
		this.taskClassification = taskClassification;
	}


	public String getTaskSubClassification() {
		return taskSubClassification;
	}


	public void setTaskSubClassification(String taskSubClassification) {
		this.taskSubClassification = taskSubClassification;
	}


	public String getTaskToBeCreated() {
		return taskToBeCreated;
	}


	public void setTaskToBeCreated(String taskToBeCreated) {
		this.taskToBeCreated = taskToBeCreated;
	}


	public Double getDailyOilValue() {
		return dailyOilValue;
	}


	public void setDailyOilValue(Double dailyOilValue) {
		this.dailyOilValue = dailyOilValue;
	}


	public Double getComparisonRatio() {
		return comparisonRatio;
	}


	public void setComparisonRatio(Double comparisonRatio) {
		this.comparisonRatio = comparisonRatio;
	}


	public String getOilMeterMerrickId() {
		return oilMeterMerrickId;
	}


	public void setOilMeterMerrickId(String oilMeterMerrickId) {
		this.oilMeterMerrickId = oilMeterMerrickId;
	}


	public String getGasMeterMerrickId() {
		return gasMeterMerrickId;
	}


	public void setGasMeterMerrickId(String gasMeterMerrickId) {
		this.gasMeterMerrickId = gasMeterMerrickId;
	}


	public String getMeterName() {
		return meterName;
	}


	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	//DailyOilValue ComparisonRatio OilMeterMerrickId GasMeterMerrickId MeterName TypeOfTaskToBeCreated Classification SubClassification

	@Override
	public ITAGasBlowByActionDto convertFromJSonNode(JsonNode node) {
		if (node != null && node.has("DailyOilValue") && node.has("ComparisonRatio") && node.has("OilMeterMerrickId")
				&& node.has("GasMeterMerrickId") && node.has("MeterName") && node.has("TypeOfTaskToBeCreated")
				&& node.has("Classification") && node.has("SubClassification")) {
			this.setTaskClassification(node.get("Classification").asText());
			this.setTaskToBeCreated(node.get("TypeOfTaskToBeCreated").asText());
			this.setTaskSubClassification(node.get("SubClassification").asText());
			this.setDailyOilValue(node.get("DailyOilValue").asDouble());
			this.setComparisonRatio(node.get("ComparisonRatio").asDouble());
			this.setOilMeterMerrickId(node.get("OilMeterMerrickId").asText());
			this.setGasMeterMerrickId(node.get("GasMeterMerrickId").asText());
			this.setMeterName(node.get("MeterName").asText());
			return this;
		}
		return null;
	}

}
