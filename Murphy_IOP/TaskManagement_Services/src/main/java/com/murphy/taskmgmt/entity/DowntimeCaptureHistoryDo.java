package com.murphy.taskmgmt.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_DOWNTIME_CAPTURE_HISTORY")
public class DowntimeCaptureHistoryDo implements BaseDo {

//	@EmbeddedId
//	private DowntimeCaptureHistoryDoPK downtimeHistoryDoPK;
	
	@Id
	@Column(name = "DTH_ID", length = 100, nullable = false)
	private String dthId = UUID.randomUUID().toString().replaceAll("-", "");
	
	@Column(name = "ALARM_CONDITION", length = 100, nullable = true)
	private String alarmCondition;

	@Column(name = "LONG_DESCRIPTION", length = 600)
	private String longDescription;

	@Column(name = "DOWNTIME_CLASSIFIER", length = 70)
	private String downTimeClassifier;

	@Column(name = "POINT_ID", length = 150)
	private String pointId;

//	public DowntimeCaptureHistoryDoPK getDownrtimeHistoryDoPK() {
//		return downtimeHistoryDoPK;
//	}
//
//	public void setDownrtimeHistoryDoPK(DowntimeCaptureHistoryDoPK downtimeHistoryDoPK) {
//		this.downtimeHistoryDoPK = downtimeHistoryDoPK;
//	}

	public String getDthId() {
		return dthId;
	}

	public void setDthId(String dthId) {
		this.dthId = dthId;
	}

	public String getDownTimeClassifier() {
		return downTimeClassifier;
	}

	public void setDownTimeClassifier(String downTimeClassifier) {
		this.downTimeClassifier = downTimeClassifier;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

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
		return "DowntimeCaptureHistoryDo [dthId=" + dthId + ", alarmCondition=" + alarmCondition + ", longDescription="
				+ longDescription + ", downTimeClassifier=" + downTimeClassifier + ", pointId=" + pointId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
