package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FlareDowntimeDto extends BaseDto {
	
	private String id;
	private String type;
	private String meter;
	private String childCode;
	private String childText;
	private String createdAt;
	private String createdBy;
	private String updatedAt;
	private String updatedBy;
	private String merrickId;
	private Date startTime;
	private Date endTime;
	private String status;
	private Double flareVolume;
	private Integer durationByRocMinute;
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

	private Integer durationByRocHour;
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

	public String getChildCode() {
		return childCode;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode;
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

	public Double getFlareVolume() {
		return flareVolume;
	}

	public void setFlareVolume(Double flareVolume) {
		this.flareVolume = flareVolume;
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

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
