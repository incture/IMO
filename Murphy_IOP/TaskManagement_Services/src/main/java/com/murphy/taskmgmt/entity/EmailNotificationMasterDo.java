package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_EMA_NOT_MAS")
public class EmailNotificationMasterDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONFIG_ID", length = 100)
	private String configId;

	@Column(name = "CONFIG_ITEM", length = 100)
	private String configItem;

	// To or CC or BCC
	@Column(name = "RECP_TYPE", length = 20)
	private String recpType;

	// user or roles
	@Column(name = "CONFIG_TYPE", length = 100)
	private String configType;

	@Column(name = "CONFIG_VALUE", length = 300)
	private String configValue;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	public String getConfigItem() {
		return configItem;
	}

	public void setConfigItem(String configItem) {
		this.configItem = configItem;
	}

	public String getRecpType() {
		return recpType;
	}

	public void setRecpType(String recpType) {
		this.recpType = recpType;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public Object getPrimaryKey() {
		return configId;
	}

	@Override
	public String toString() {
		return "EmailNotificationMasterDo [configId=" + configId + ", configItem=" + configItem + ", recpType="
				+ recpType + ", configType=" + configType + ", configValue=" + configValue + "]";
	}
	

}
