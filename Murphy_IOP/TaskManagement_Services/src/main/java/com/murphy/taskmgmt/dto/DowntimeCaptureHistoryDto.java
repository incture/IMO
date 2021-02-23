package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeCaptureHistoryDto extends BaseDto {

	private String dthId;
	private String longDescription;
	private String downTimeClassifier;
	private String alarmCondition;
	private String pointId;
	private String locationCode;

	public String getDthId() {
		return dthId;
	}

	public void setDthId(String dthId) {
		this.dthId = dthId;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getDownTimeClassifier() {
		return downTimeClassifier;
	}

	public void setDownTimeClassifier(String downTimeClassifier) {
		this.downTimeClassifier = downTimeClassifier;
	}

	public String getAlarmCondition() {
		return alarmCondition;
	}

	public void setAlarmCondition(String alarmCondition) {
		this.alarmCondition = alarmCondition;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Override
	public String toString() {
		return "DowntimeCaptureHistoryDto [dthId=" + dthId + ", longDescription=" + longDescription
				+ ", downTimeClassifier=" + downTimeClassifier + ", alarmCondition=" + alarmCondition + ", pointId="
				+ pointId + ", locationCode=" + locationCode + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
