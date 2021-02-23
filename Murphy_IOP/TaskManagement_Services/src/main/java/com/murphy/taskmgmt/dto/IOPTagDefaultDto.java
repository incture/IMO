package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class IOPTagDefaultDto extends BaseDto {

	private int tagId;
	private String tagName;
	private String displayName;
	private String muwi;
	private String wellName;
	private String aggregationName;
	private String reportId;
	private String reportName;
	private String yAxis;
	private String unit;
	
	
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getAggregationName() {
		return aggregationName;
	}
	public void setAggregationName(String aggregationName) {
		this.aggregationName = aggregationName;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getyAxis() {
		return yAxis;
	}
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
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
	@Override
	public String toString() {
		return "IOPTagDefaultDto [tagId=" + tagId + ", tagName=" + tagName + ", displayName=" + displayName + ", muwi="
				+ muwi + ", wellName=" + wellName + ", aggregationName=" + aggregationName + ", reportId=" + reportId
				+ ", reportName=" + reportName + ", yAxis=" + yAxis + ", unit=" + unit + "]";
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
