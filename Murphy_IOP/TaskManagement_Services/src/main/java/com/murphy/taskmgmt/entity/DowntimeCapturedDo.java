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
@Table(name = "DOWNTIME_CAPTURE")
public class DowntimeCapturedDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	
	
	@Id
	@Column(name = "ID", length = 32)
	private String id;

	@Column(name = "TYPE", length = 50)
	private String type;
	
	@Column(name = "WELL", length = 50)
	private String well;
	
	@Column(name = "FACILITY", length = 50)
	private String facility;
	
	@Column(name = "DOWNTIME_CODE")
	private Integer downtimeCode;
	
	@Column(name = "CHILD_CODE")
	private Integer childCode;
	
	
	@Column(name = "DOWNTIME_TEXT", length = 50)
	private String downtimeText;
	
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
	
	@Column(name = "MUWI", length = 32)
	private String muwi;
	
	@Column(name = "EQUIPMENT_ID", length = 50)
	private String equipmentId;
	
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
	
	@Column(name = "POINT_ID", length = 300)
	private String pointId;
	
	@Column(name = "COUNTRY_CODE", length = 10)
	private String countryCode;
	
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


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	@Override
	public String toString() {
		return "DowntimeCapturedDo [id=" + id + ", type=" + type + ", well=" + well + ", facility=" + facility
				+ ", downtimeCode=" + downtimeCode + ", childCode=" + childCode + ", downtimeText=" + downtimeText
				+ ", childText=" + childText + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt="
				+ updatedAt + ", updatedBy=" + updatedBy + ", muwi=" + muwi + ", equipmentId=" + equipmentId
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", durationInMinute="
				+ durationInMinute + ", durationByRoc=" + durationByRoc + ", pointId=" + pointId + ", countryCode="
				+ countryCode + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
