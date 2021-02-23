package com.murphy.taskmgmt.dto;

import java.util.Date;

public class PlotlyRequestDto {

	private String reportId;
	private String muwi;
	private String wellName;
	private String location;
	private Date startDate;
	private Date endDate;
	private int duration;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public String getWellName() {
		return wellName;
	}

	public void setWellName(String wellName) {
		this.wellName = wellName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public String toString() {
		return "PlotlyRequestDto [reportId=" + reportId + ", muwi=" + muwi + ", wellName=" + wellName + ", countryCode="
				+ location + ", startDate=" + startDate + ", endDate=" + endDate + ", duration=" + duration + "]";
	}

	

}
