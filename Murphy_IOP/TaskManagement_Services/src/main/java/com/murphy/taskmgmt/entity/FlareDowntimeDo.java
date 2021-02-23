package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FLARE_DOWNTIME")
public class FlareDowntimeDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 32)
	private String id;

	@Column(name = "TYPE", length = 50)
	private String type;

	@Column(name = "METER", length = 50)
	private String meter;

	@Column(name = "CHILD_CODE")
	private String childCode;

	@Column(name = "CHILD_TEXT", length = 50)
	private String childText;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Column(name = "UPDATED_BY", length = 100)
	private String updatedBy;

	@Column(name = "MERRICK_ID", length = 32)
	private String merrickId;

	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "STATUS", length = 50)
	private String status;

	@Column(name = "DURATION_MINUTE")
	private Integer durationInMinute;

	@Column(name = "DURATION_MINUTE_ROC")
	private Integer durationByRoc;

	@Column(name = "FLARE_VOLUME")
	private Double flareVolume;

	
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
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

	public Integer getDurationInMinute() {
		return durationInMinute;
	}

	public void setDurationInMinute(Integer durationInMinute) {
		this.durationInMinute = durationInMinute;
	}

	public Integer getDurationByRoc() {
		return durationByRoc;
	}

	public void setDurationByRoc(Integer durationByRoc) {
		this.durationByRoc = durationByRoc;
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

	@Override
	public String toString() {
		return "DowntimeCapturedDo [id=" + id + ", type=" + type + ", meter=" + meter 
				+ ", childCode=" + childCode + ", childText=" + childText + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt="
				+ updatedAt + ", updatedBy=" + updatedBy + ", merrickId=" + merrickId + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", durationInMinute="
				+ durationInMinute + ", durationByRoc=" + durationByRoc + ", flareVolume" + flareVolume + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
