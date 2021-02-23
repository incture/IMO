package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DowntimeCaptureHistoryDoPK implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3394252745910338113L;

	@Column(name = "ALARM_CONDITION", length = 100)
	private String alarmCondition;

	@Column(name = "LONG_DESCRIPTION", length = 600)
	private String longDescription;

	public String getAlarmCondition() {
		return alarmCondition;
	}

	public void setAlarmCondition(String alarmCondition) {
		this.alarmCondition = alarmCondition;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	@Override
	public String toString() {
		return "DowntimeCaptureHistoryDoPK [alarmCondition=" + alarmCondition + ", longDescription="
				+ longDescription + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
