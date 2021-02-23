package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CYGNET_DOWNTIME_COUNTER")
public class CygnetDowntimeCounterDo implements BaseDo{
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "POINT_ID", length = 100)
	private String pointId;
	
	@Column(name = "ALARM_SEVERITY", length = 100)
	private String alarmSeverity;
	
	@Column(name = "TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	
    

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

	public String getAlarmSeverity() {
		return alarmSeverity;
	}

	public void setAlarmSeverity(String alarmSeverity) {
		this.alarmSeverity = alarmSeverity;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}

	@Override
	public String toString() {
		return "CygnetDowntimeCounterDo [id=" + id + ", pointId=" + pointId + ", alarmSeverity=" + alarmSeverity
				+ ", timeStamp=" + timeStamp + "]";
	}
	

}
