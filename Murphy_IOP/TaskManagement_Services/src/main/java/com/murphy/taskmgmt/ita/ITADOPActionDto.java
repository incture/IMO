package com.murphy.taskmgmt.ita;

import com.fasterxml.jackson.databind.JsonNode;

public class ITADOPActionDto implements RuleOutputDto {
	private String taskClassification;
	private String taskSubClassification;
	private String tierLevel;
	private Double percentageDeviation;
	private String typeOfTaskToBeCreated;
	private Double actualBO;
	private String ssvStatus;
	private int from;
	private int to;
	
	public String getSsvStatus() {
		return ssvStatus;
	}

	public void setSsvStatus(String ssvStatus) {
		this.ssvStatus = ssvStatus;
	}

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

	public String getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}

	public Double getPercentageDeviation() {
		return percentageDeviation;
	}

	public void setPercentageDeviation(Double percentageDeviation) {
		this.percentageDeviation = percentageDeviation;
	}

	public String getTypeOfTaskToBeCreated() {
		return typeOfTaskToBeCreated;
	}

	public void setTypeOfTaskToBeCreated(String typeOfTaskToBeCreated) {
		this.typeOfTaskToBeCreated = typeOfTaskToBeCreated;
	}

	public Double getActualBO() {
		return actualBO;
	}

	public void setActualBO(Double actualBO) {
		this.actualBO = actualBO;
	}


	@Override
	public ITADOPActionDto convertFromJSonNode(JsonNode node) {
		if (node != null && node.has("TaskClassification") && node.has("TimeFrom") && node.has("BarrelsOfOil")
				&& node.has("TypeOfTaskToBeCreated") && node.has("Percentagedeviation") && node.has("Tier")
				&& node.has("TaskSubClassification") && node.has("SSV") && node.has("TimeTo")) {
			this.setTaskClassification(node.get("TaskClassification").asText());
			this.setActualBO(node.get("BarrelsOfOil").asDouble());
			this.setTypeOfTaskToBeCreated(node.get("TypeOfTaskToBeCreated").asText());
			this.setPercentageDeviation(node.get("Percentagedeviation").asDouble());
			this.setTierLevel(node.get("Tier").asText());
			this.setTaskSubClassification(node.get("TaskSubClassification").asText());
			this.setSsvStatus(node.get("SSV").asText());
			this.setFrom(node.get("TimeFrom").asInt());
			this.setTo(node.get("TimeTo").asInt());
			return this;
		}
		return null;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

}
