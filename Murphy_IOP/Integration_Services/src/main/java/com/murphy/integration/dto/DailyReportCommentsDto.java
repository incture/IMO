package com.murphy.integration.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DailyReportCommentsDto {

	private String area;
	private String pad;
	private String wellName;
	private String grossboed;
	private String netboed;
	private String comments;
	private String foremanName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private String timeStamp;
	private String productionUnit;
	private String asset;
	private String muwi;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPad() {
		return pad;
	}
	public void setPad(String pad) {
		this.pad = pad;
	}
	public String getWellName() {
		return wellName;
	}
	public void setWellName(String wellName) {
		this.wellName = wellName;
	}
	public String getGrossboed() {
		return grossboed;
	}
	public void setGrossboed(String grossboed) {
		this.grossboed = grossboed;
	}
	public String getNetboed() {
		return netboed;
	}
	public void setNetboed(String netboed) {
		this.netboed = netboed;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getForemanName() {
		return foremanName;
	}
	public void setForemanName(String foremanName) {
		this.foremanName = foremanName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getProductionUnit() {
		return productionUnit;
	}
	public void setProductionUnit(String productionUnit) {
		this.productionUnit = productionUnit;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	@Override
	public String toString() {
		return "DailyReportCommentsDto [area=" + area + ", pad=" + pad + ", wellName=" + wellName + ", grossboed="
				+ grossboed + ", netboed=" + netboed + ", comments=" + comments + ", foremanName=" + foremanName
				+ ", date=" + date + ", timeStamp=" + timeStamp + ", productionUnit=" + productionUnit + ", asset="
				+ asset + ", muwi=" + muwi + "]";
	}
	
	
}
