package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CygnetAlarmFeedDto extends BaseDto {

	private String pointId;
	private Date timeStamp;
	private String timeStampAsString;
	private String facDescription;
	private String longDescription;
	private String tier;
	private String route;
	private String alarmCondition;
	private String alarmValue;
	private String unacknowledged;
	private String acknowledged;
	private String suppressed;
	private String hidden;
	private String alarmSeverity;
	private String muwi;
	private String field;
	private String facility;
	private String tag;
	private String downTimeClassifier;
	private String isAcknowledge;
	private String isDesignate;
	private String isDispatch;
	private String cygnetRecomendedTime = "-";
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String locationCode;
	private String locationType;
	private String timeZone;

	public String getCygnetRecomendedTime() {
		return cygnetRecomendedTime;
	}

	public void setCygnetRecomendedTime(String cygnetRecomendedTime) {
		this.cygnetRecomendedTime = cygnetRecomendedTime;
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

	public String getTimeStampAsString() {
		return timeStampAsString;
	}

	public void setTimeStampAsString(String timeStampAsString) {
		this.timeStampAsString = timeStampAsString;
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

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		return "CygnetAlarmFeedDto [pointId=" + pointId + ", timeStamp=" + timeStamp + ", timeStampAsString="
				+ timeStampAsString + ", facDescription=" + facDescription + ", longDescription=" + longDescription
				+ ", tier=" + tier + ", route=" + route + ", alarmCondition=" + alarmCondition + ", alarmValue="
				+ alarmValue + ", unacknowledged=" + unacknowledged + ", acknowledged=" + acknowledged + ", suppressed="
				+ suppressed + ", hidden=" + hidden + ", alarmSeverity=" + alarmSeverity + ", muwi=" + muwi + ", field="
				+ field + ", facility=" + facility + ", tag=" + tag + ", downTimeClassifier=" + downTimeClassifier
				+ ", isAcknowledge=" + isAcknowledge + ", isDesignate=" + isDesignate + ", isDispatch=" + isDispatch
				+ ", cygnetRecomendedTime=" + cygnetRecomendedTime + ", latitude=" + latitude + ", longitude="
				+ longitude + ", locationCode=" + locationCode + ", locationType=" + locationType + ", timeZone="
				+ timeZone + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}
