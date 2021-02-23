package com.murphy.taskmgmt.ita;

import com.fasterxml.jackson.databind.JsonNode;

public class ITATaskActionDto implements RuleOutputDto {
	private int durationInDays;
	private int numberOfTasksCreated;
	private String Classification;
	private String subClassification;
	private String rootCause;
	private String typeOfTaskToBeCreated;
	private String taskClassificationITA;
	private String taskSubClassificationITA;

	public int getDurationInDays() {
		return durationInDays;
	}

	public void setDurationInDays(int durationInDays) {
		this.durationInDays = durationInDays;
	}

	public int getNumberOfTasksCreated() {
		return numberOfTasksCreated;
	}

	public void setNumberOfTasksCreated(int numberOfTasksCreated) {
		this.numberOfTasksCreated = numberOfTasksCreated;
	}

	public String getClassification() {
		return Classification;
	}

	public void setClassification(String classification) {
		Classification = classification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public String getTypeOfTaskToBeCreated() {
		return typeOfTaskToBeCreated;
	}

	public void setTypeOfTaskToBeCreated(String typeOfTaskToBeCreated) {
		this.typeOfTaskToBeCreated = typeOfTaskToBeCreated;
	}

	public String getTaskClassificationITA() {
		return taskClassificationITA;
	}

	public void setTaskClassificationITA(String taskClassificationITA) {
		this.taskClassificationITA = taskClassificationITA;
	}

	public String getTaskSubClassificationITA() {
		return taskSubClassificationITA;
	}

	public void setTaskSubClassificationITA(String taskSubClassificationITA) {
		this.taskSubClassificationITA = taskSubClassificationITA;
	}

	@Override
	public ITATaskActionDto convertFromJSonNode(JsonNode node) {
		if (node != null && node.has("DurationInDays") && node.has("NumberOfTasksCreated") && node.has("Classification")
				&& node.has("SubClassification") && node.has("TypeOfTaskToBeCreated") && node.has("TaskClassification")
				&& node.has("TaskSubClassification")) {
			this.setDurationInDays(node.get("DurationInDays").asInt());
			this.setNumberOfTasksCreated(node.get("NumberOfTasksCreated").asInt());
			this.setClassification(node.get("Classification").asText());
			this.setSubClassification(node.get("SubClassification").asText());
			this.setRootCause(node.get("TaskRootCause").asText());
			this.setTypeOfTaskToBeCreated(node.get("TypeOfTaskToBeCreated").asText());
			this.setTaskClassificationITA(node.get("TaskClassification").asText());
			this.setTaskSubClassificationITA(node.get("TaskSubClassification").asText());
			return this;
		}
		return null;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

}
