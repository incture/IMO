package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ProductionVarianceDto extends BaseDto {

	private String well;
	private Date productionDate;
	private String versionName;
	private Double forecastBoed;
	private String muwi;
	private Date createdAt;
	private String paramType;
	private String source;
	private double dataValue;
	
	public String getWell() {
		return well;
	}
	public void setWell(String well) {
		this.well = well;
	}
	public Date getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Double getForecastBoed() {
		return forecastBoed;
	}
	public void setForecastBoed(Double forecastBoed) {
		this.forecastBoed = forecastBoed;
	}
	@Override
	public String toString() {
		return "ProductionVarianceDto [well=" + well + ", productionDate=" + productionDate + ", versionName="
				+ versionName + ", forecastBoed=" + forecastBoed + ", muwi=" + muwi + ", createdAt=" + createdAt
				+ ", paramType=" + paramType + ", source=" + source + ", dataValue=" + dataValue + "]";
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
	public String getMuwi() {
		return muwi;
	}
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getDataValue() {
		return dataValue;
	}
	public void setDataValue(double dataValue) {
		this.dataValue = dataValue;
	}
	
}


