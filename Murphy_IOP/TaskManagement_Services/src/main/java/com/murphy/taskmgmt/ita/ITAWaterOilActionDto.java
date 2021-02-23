package com.murphy.taskmgmt.ita;

import com.fasterxml.jackson.databind.JsonNode;

public class ITAWaterOilActionDto implements RuleOutputDto {
	private String taskClassification;
	private String taskSubClassification;
	private String taskToBeCreated;
	private Double oilThreshold;
	private Double waterThreshold;
	private String tier;
	
	public String getTaskClassification() {
		return taskClassification;
	}
	public void setTaskClassification(String taskClassification) {
		this.taskClassification = taskClassification;
	}
	public String getTaskSubClassification() {
		return taskSubClassification;
	}
	public String getTaskToBeCreated() {
		return taskToBeCreated;
	}
	public void setTaskToBeCreated(String taskToBeCreated) {
		this.taskToBeCreated = taskToBeCreated;
	}
	public void setTaskSubClassification(String taskSubClassification) {
		this.taskSubClassification = taskSubClassification;
	}
	public Double getOilThreshold() {
		return oilThreshold;
	}
	public void setOilThreshold(Double oilThreshold) {
		this.oilThreshold = oilThreshold;
	}
	public Double getWaterThreshold() {
		return waterThreshold;
	}
	public void setWaterThreshold(Double waterThreshold) {
		this.waterThreshold = waterThreshold;
	}
	
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	@Override
	public ITAWaterOilActionDto convertFromJSonNode(JsonNode node) {
		if (node != null && node.has("TaskClassification") && node.has("WaterThreshold") && node.has("OilThreshold")
				&& node.has("TaskToBeCreated") && node.has("TaskSubClassification")) {
			this.setTaskClassification(node.get("TaskClassification").asText());
			this.setOilThreshold(node.get("OilThreshold").asDouble());
			this.setWaterThreshold(node.get("WaterThreshold").asDouble());
			this.setTaskToBeCreated(node.get("TaskToBeCreated").asText());
			this.setTaskSubClassification(node.get("TaskSubClassification").asText());
			this.setTier(node.get("Tier").asText());
			return this;
		}
		return null;
	}

}
