package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UPLIFT_CONFIG_MASTER")
public class UpliftConfigMasterDo  implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "WEATHER_CON_CODE")
	private Integer weatherConditionCode;
	
	@Column(name = "DESCRIPTION", length = 255)
	private String description;
	
	@Column(name="UPLIFT_PERCENTAGE")
	private Double upliftPercentage;

	public Integer getWeatherConditionCode() {
		return weatherConditionCode;
	}

	public void setWeatherConditionCode(Integer weatherConditionCode) {
		this.weatherConditionCode = weatherConditionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getUpliftPercentage() {
		return upliftPercentage;
	}

	public void setUpliftPercentage(Double upliftPercentage) {
		this.upliftPercentage = upliftPercentage;
	}

	@Override
	public String toString() {
		return "UpliftConfigMasterDo [weatherConditionCode=" + weatherConditionCode + ", description=" + description
				+ ", upliftPercentage=" + upliftPercentage + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return weatherConditionCode;
	}
	
	

}
