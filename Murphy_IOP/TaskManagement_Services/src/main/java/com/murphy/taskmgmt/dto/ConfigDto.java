package com.murphy.taskmgmt.dto;

public class ConfigDto {

	private String configId;
//	private String configType;
//	private String configDesc;
	private String configValue;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	@Override
	public String toString() {
		return "ConfigDto [configId=" + configId + ", configValue=" + configValue + "]";
	}

}
