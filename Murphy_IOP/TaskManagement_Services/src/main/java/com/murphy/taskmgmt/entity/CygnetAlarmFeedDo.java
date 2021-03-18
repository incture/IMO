package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_CYGNET_ALARM_FEED")
public class CygnetAlarmFeedDo implements BaseDo {

	@Id
	@Column(name = "POINT_ID", length = 300)
	private String pointId;

	@Column(name = "TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	@Column(name = "FAC_DESCRIPTION", length = 300)
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

	@Column(name = "UNACKNOWLEDGED", length = 5)
	private String unacknowledged;

	@Column(name = "ACKNOWLEDGED", length = 5)
	private String acknowledged;

	@Column(name = "SUPPRESSED", length = 5)
	private String suppressed;

	@Column(name = "HIDDEN", length = 5)
	private String hidden;

	@Column(name = "ALARM_SEVERITY", length = 20)
	private String alarmSeverity;

	@Column(name = "MUWI", length = 100)
	private String muwi;

	@Column(name = "FIELD", length = 100)
	private String field;

	@Column(name = "FACILITY", length = 100)
	private String facility;

	@Column(name = "TAG", length = 50)
	private String tag;

	@Column(name = "DOWNTIME_CLASSIFIER", length = 70)
	private String downTimeClassifier;

	@Column(name = "IS_ACKNOWLEDGE", length = 5)
	private String isAcknowledge;

	@Column(name = "IS_DESIGNATE", length = 5)
	private String isDesignate;

	@Column(name = "IS_DISPATCH", length = 5)
	private String isDispatch;

	@Override
	public Object getPrimaryKey() {

		return pointId;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
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

	public String getUnacknowledged() {
		return unacknowledged;
	}

	public void setUnacknowledged(String unacknowledged) {
		this.unacknowledged = unacknowledged;
	}

	public String getAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(String acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getSuppressed() {
		return suppressed;
	}

	public void setSuppressed(String suppressed) {
		this.suppressed = suppressed;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getAlarmSeverity() {
		return alarmSeverity;
	}

	public void setAlarmSeverity(String alarmSeverity) {
		this.alarmSeverity = alarmSeverity;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDownTimeClassifier() {
		return downTimeClassifier;
	}

	public void setDownTimeClassifier(String downTimeClassifier) {
		this.downTimeClassifier = downTimeClassifier;
	}

	public String getIsAcknowledge() {
		return isAcknowledge;
	}

	public void setIsAcknowledge(String isAcknowledge) {
		this.isAcknowledge = isAcknowledge;
	}

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

	@Override
	public String toString() {
		return "AlarmFeedDo [pointId=" + pointId + ", timeStamp=" + timeStamp + ", facDescription=" + facDescription
				+ ", longDescription=" + longDescription + ", tier=" + tier + ", route=" + route + ", alarmCondition="
				+ alarmCondition + ", alarmValue=" + alarmValue + ", unacknowledged=" + unacknowledged
				+ ", acknowledged=" + acknowledged + ", suppressed=" + suppressed + ", hidden=" + hidden
				+ ", alarmSeverity=" + alarmSeverity + ", muwi=" + muwi + ", field=" + field + ", facility=" + facility
				+ ", tag=" + tag + ", downTimeClassifier=" + downTimeClassifier + ", isAcknowledge=" + isAcknowledge
				+ ", isDesignate=" + isDesignate + ", isDispatch=" + isDispatch + "]";
	}

}
