package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PRODUCTION_VARIANCE")
public class ProductionVarianceDo implements BaseDo {
	
	@Id
	@Column(name = "VARIANCE_ID", length = 40)
	private String varianceId=UUID.randomUUID().toString().replaceAll("-", "");
	
	@Column(name = "WELL", length = 100)
	private String well;

	@Column(name = "PRODUCTION_DATE")
	@Temporal(TemporalType.DATE)
	private Date productionDate;
	
	@Column(name = "VERSION_NAME", length = 80)
	private String versionName;
	
	@Column(name = "FORECAST_BOED")
	private Double forecastBoed;
	
	@Column(name = "MUWI_ID", length = 100)
	private String muwi;

	public String getWell() {
		return well;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
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
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "ProductionVarianceDo [well=" + well + ", productionDate=" + productionDate + ", versionName="
				+ versionName + ", forecastBoed=" + forecastBoed + "]";
	}
	
	
}
