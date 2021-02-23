package com.murphy.taskmgmt.dto;



import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeCapturedDto extends BaseDto{
	

	private String id;
	private String type;
	private String well;
	private String facility;
	private Integer downtimeCode;
	private Integer childCode;
	private String downtimeText;
	private String childText;
	private String createdAt;
	private String createdBy;
	private String updatedAt;
	private String updatedBy;
	private String muwi;
	private String equipmentId;
	private Date startTime;
	private Date endTime;
	private String status;
	private Integer durationByCygnateMinute;
	private Integer durationByCygnateHours;
	private Integer durationByRocMinute;
	private Integer durationByRocHour;
	private String pointId;
	private String createdAtUTC;
	private String inMilliSec;
	private int merrickId;
	private String countryCode;
	
	
	public int getMerrickId() {
		return merrickId;
	}
	public void setMerrickId(int merrickId) {
		this.merrickId = merrickId;
	}
	public String getCreatedAtUTC() {
		return createdAtUTC;
	}
	public void setCreatedAtUTC(String createdAtUTC) {
		this.createdAtUTC = createdAtUTC;
	}
	public String getInMilliSec() {
		return inMilliSec;
	}
	public void setInMilliSec(String inMilliSec) {
		this.inMilliSec = inMilliSec;
	}
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	public String getDowntimeText() {
		return downtimeText;
	}
	public void setDowntimeText(String downtimeText) {
		this.downtimeText = downtimeText;
	}
	public String getChildText() {
		return childText;
	}
	public void setChildText(String childText) {
		this.childText = childText;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWell() {
		return well;
	}
	public void setWell(String well) {
		this.well = well;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public Integer getDowntimeCode() {
		return downtimeCode;
	}
	public void setDowntimeCode(Integer downtimeCode) {
		this.downtimeCode = downtimeCode;
	}
	public Integer getChildCode() {
		return childCode;
	}
	public void setChildCode(Integer childCode) {
		this.childCode = childCode;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDurationByCygnateMinute() {
		return durationByCygnateMinute;
	}
	public void setDurationByCygnateMinute(Integer durationByCygnateMinute) {
		this.durationByCygnateMinute = durationByCygnateMinute;
	}
	public Integer getDurationByCygnateHours() {
		return durationByCygnateHours;
	}
	public void setDurationByCygnateHours(Integer durationByCygnateHours) {
		this.durationByCygnateHours = durationByCygnateHours;
	}
	public Integer getDurationByRocMinute() {
		return durationByRocMinute;
	}
	public void setDurationByRocMinute(Integer durationByRocMinute) {
		this.durationByRocMinute = durationByRocMinute;
	}
	public Integer getDurationByRocHour() {
		return durationByRocHour;
	}
	public void setDurationByRocHour(Integer durationByRocHour) {
		this.durationByRocHour = durationByRocHour;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		
	}
	@Override
	public String toString() {
		return "DowntimeCapturedDto [id=" + id + ", type=" + type + ", well=" + well + ", facility=" + facility
				+ ", downtimeCode=" + downtimeCode + ", childCode=" + childCode + ", downtimeText=" + downtimeText
				+ ", childText=" + childText + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt="
				+ updatedAt + ", updatedBy=" + updatedBy + ", muwi=" + muwi + ", equipmentId=" + equipmentId
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status
				+ ", durationByCygnateMinute=" + durationByCygnateMinute + ", durationByCygnateHours="
				+ durationByCygnateHours + ", durationByRocMinute=" + durationByRocMinute + ", durationByRocHour="
				+ durationByRocHour + ", pointId=" + pointId + ", createdAtUTC=" + createdAtUTC + ", inMilliSec="
				+ inMilliSec + ", merrickId=" + merrickId + ", countryCode=" + countryCode + "]";
	}
	
	
	
}
