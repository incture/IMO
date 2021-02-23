package com.murphy.taskmgmt.dto;
import java.util.Date;

public class CustomDowntimeResponseDto implements Comparable<CustomDowntimeResponseDto> {

	private String id;
	private String type;
	private String well;
	private String facility;
	private String downtimeText;
	private String childCode;
	private String childText;
	private String createdAt;
	private String muwi;
	private String equipmentId;
	private Date startTime;
	private Date endTime;
	private String status;
	private Integer durationByCygnateMinute;
	private Integer durationByCygnateHours;
	private Integer durationByRocMinute;
	private Integer durationByRocHour;
	
	private Double flareVolume;
	private String meter;
	private String merrickId;
	private String created_At;
	private String createdAtUTC;
	private String inMilliSec;
	
	
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
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
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
	public void setDurationByRocMinute(Integer durationByRocMinute) {
		this.durationByRocMinute = durationByRocMinute;
	}
	public Integer getDurationByRocHour() {
		return durationByRocHour;
	}
	public void setDurationByRocHour(Integer durationByRocHour) {
		this.durationByRocHour = durationByRocHour;
	}
	public Double getFlareVolume() {
		return flareVolume;
	}
	public void setFlareVolume(Double flareVolume) {
		this.flareVolume = flareVolume;
	}
	public String getMeter() {
		return meter;
	}
	public void setMeter(String meter) {
		this.meter = meter;
	}
	public String getMerrickId() {
		return merrickId;
	}
	public void setMerrickId(String merrickId) {
		this.merrickId = merrickId;
	}
	public String getCreated_At() {
		return created_At;
	}
	public void setCreated_At(String created_At) {
		this.created_At = created_At;
	}
	
	
	@Override
	public String toString() {
		return "CustomDowntimeResponseDto [id=" + id + ", type=" + type + ", well=" + well + ", facility=" + facility
				+ ", downtimeText=" + downtimeText + ", childCode=" + childCode + ", childText=" + childText
				+ ", createdAt=" + createdAt + ", muwi=" + muwi + ", equipmentId=" + equipmentId + ", startTime="
				+ startTime + ", endTime=" + endTime + ", status=" + status + ", durationByCygnateMinute="
				+ durationByCygnateMinute + ", durationByCygnateHours=" + durationByCygnateHours
				+ ", durationByRocMinute=" + durationByRocMinute + ", durationByRocHour=" + durationByRocHour
				+ ", flareVolume=" + flareVolume + ", meter=" + meter
				+ ", merrickId=" + merrickId + ", created_At=" + created_At + ", createdAtUTC=" + createdAtUTC
				+ ", inMilliSec=" + inMilliSec + "]";
	}
	
	
	
	@Override
	public int compareTo(CustomDowntimeResponseDto o) {
		return this.getCreated_At().compareTo(o.getCreated_At());
	}
	public String getChildCode() {
		return childCode;
	}
	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}
	
	
	
}
