package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_CONFIG_VALUES")
public class ConfigValuesDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONFIG_ID", length = 200)
	private String configId;

	@Column(name = "CONFIG_DESC_VALUE", length = 300)
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
		return "ConfigValuesDo [configId=" + configId + ", configValue=" + configValue + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return configId;
	}

}
