package com.murphy.taskmgmt.dto;

//import java.sql.Timestamp;
import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class CygnetRecomDowntimeDto  extends BaseDto{
	
	private String id;
	private String pointId;
	private Date startTime;
	private Date endTime;
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
		return "CygnetRecomDowntimeDto [id=" + id + ", pointId=" + pointId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", cygnetRecomDuration=" + cygnetRecomDuration + "]";
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
