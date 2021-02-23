package com.murphy.integration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EnersightProveDailyDto {

	private String well;
	private String lastProdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date lastProdDateField;
	private double forecastBoed;
	private String muwiId;
	
	public String getWell() {
		return well;
	}
	public void setWell(String well) {
		this.well = well;
	}
	public String getLastProdDate() {
		return lastProdDate;
	}
	public void setLastProdDate(String lastProdDate) {
		this.lastProdDate = lastProdDate;
	}
	public Date getLastProdDateField() {
		return lastProdDateField;
	}
	public void setLastProdDateField(Date lastProdDateField) {
		this.lastProdDateField = lastProdDateField;
	}
	public double getForecastBoed() {
		return forecastBoed;
	}
	public void setForecastBoed(double forecastBoed) {
		this.forecastBoed = forecastBoed;
	}
	
	public String getMuwiId() {
		return muwiId;
	}
	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}
	
}
