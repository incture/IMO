package com.murphy.taskmgmt.entity;

//import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CYGNET_RECOM_DOWNTIME")
public class CygnetRecomDowntimeDo implements BaseDo{
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "POINT_ID", length = 100)
	private String pointId;
		
	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name = "CYGNET_RECOM_DURATION")
	private int cygnetRecomDuration;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
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

	
	public int getCygnetRecomDuration() {
		return cygnetRecomDuration;
	}

	public void setCygnetRecomDuration(int cygnetRecomDuration) {
		this.cygnetRecomDuration = cygnetRecomDuration;
	}

	@Override
	public String toString() {
		return "CygnetRecomDowntimeDo [id=" + id + ", pointId=" + pointId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", cygnetRecomDuration=" + cygnetRecomDuration + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}
	
	

}
