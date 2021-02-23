package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_ALARM_FEED")
public class AlarmFeedDo implements BaseDo {

	@Id
	@Column(name = "ALARM_ID", length = 100)
	private String alarmId;

	@Column(name = "ALARM_COLOR_CODE", length = 50)
	private String alarmColorCode;

	@Column(name = "DOWNTIME_CLASSIFIER", length = 70)
	private String downTimeClassifier;

	@Column(name = "FACILITY", length = 100)
	private String facility;

	@Column(name = "MUWI", length = 100)
	private String muwi;
	
	@Column(name = "IS_ACKNOWLEDGE", length = 5)
	private String isAcknowledge;
	
	@Column(name = "IS_DESIGNATE", length = 5)
	private String isDesignate;
	
	@Column(name = "IS_DISPATCH", length = 5)
	private String isDispatch;

	@Column(name = "CREATED_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endAt;

	@Column(name = "FAC_DESCRIPTION", length = 200)
	private String facDescription;

	@Column(name = "LONG_DESCRIPTION", length = 600)
	private String longDescription;

	@Column(name = "TIER", length = 50)
	private String tier;

	@Column(name = "ROUTE", length = 70)
	private String route;

	@Column(name = "ALARM_CONDITION", length = 100)
	private String alarmCondition;

	@Column(name = "VALUE", length = 100)
	private String alarmValue;

	
	
	
	public String getIsDesignate() {
		return isDesignate;
	}

	public void setIsDesignate(String isDesignate) {
		this.isDesignate = isDesignate;
	}

	

	public String getIsDispatch() {
		return isDispatch;
	}

	public void setIsDispatch(String isDispatch) {
		this.isDispatch = isDispatch;
	}

	public void setIsAcknowledge(String isAcknowledge) {
		this.isAcknowledge = isAcknowledge;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmColorCode() {
		return alarmColorCode;
	}

	public void setAlarmColorCode(String alarmColorCode) {
		this.alarmColorCode = alarmColorCode;
	}

	public String getDownTimeClassifier() {
		return downTimeClassifier;
	}

	public void setDownTimeClassifier(String downTimeClassifier) {
		this.downTimeClassifier = downTimeClassifier;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public String getFacDescription() {
		return facDescription;
	}

	public void setFacDescription(String facDescription) {
		this.facDescription = facDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getAlarmCondition() {
		return alarmCondition;
	}

	public void setAlarmCondition(String alarmCondition) {
		this.alarmCondition = alarmCondition;
	}

	public String getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}

	@Override
	public Object getPrimaryKey() {
		return alarmId;
	}


	public String getIsAcknowledge() {
		return isAcknowledge;
	}

	@Override
	public String toString() {
		return "AlarmFeedDo [alarmId=" + alarmId + ", alarmColorCode=" + alarmColorCode + ", downTimeClassifier="
				+ downTimeClassifier + ", facility=" + facility + ", muwi=" + muwi + ", isAcknowledge=" + isAcknowledge
				+ ", isDesignate=" + isDesignate + ", isDispatch=" + isDispatch + ", createdAt=" + createdAt
				+ ", endAt=" + endAt + ", facDescription=" + facDescription + ", longDescription=" + longDescription
				+ ", tier=" + tier + ", route=" + route + ", alarmCondition=" + alarmCondition + ", alarmValue="
				+ alarmValue + "]";
	}

	
	
}
