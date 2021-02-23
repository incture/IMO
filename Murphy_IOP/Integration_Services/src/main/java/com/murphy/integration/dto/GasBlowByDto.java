package com.murphy.integration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GasBlowByDto {

	private String muwiId;
	private String wellName;
	private Integer merrickID;
	private Double estOilVol;
	private Double estGasVol;
	private Double totEstOilVol;
	private Double totEstGasVol;
	private Double avgYearlyGas;
	private String dayCount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date recordDate;
	private String oilMeterName;
	private String gasMeterName;
	private Integer oilMeterMID;
	private Integer gasMeterMID;
	
	private Double dailyOilGasRatio;
	private Double yearlyOilGasRatio;
	
	

	

	public String getMuwiId() {
		return muwiId;
	}





	public void setMuwiId(String muwiId) {
		this.muwiId = muwiId;
	}





	public String getWellName() {
		return wellName;
	}





	public void setWellName(String wellName) {
		this.wellName = wellName;
	}





	public Integer getMerrickID() {
		return merrickID;
	}





	public void setMerrickID(Integer merrickID) {
		this.merrickID = merrickID;
	}





	public Double getEstOilVol() {
		return estOilVol;
	}





	public void setEstOilVol(Double estOilVol) {
		this.estOilVol = estOilVol;
	}





	public Double getEstGasVol() {
		return estGasVol;
	}





	public void setEstGasVol(Double estGasVol) {
		this.estGasVol = estGasVol;
	}





	public Double getTotEstOilVol() {
		return totEstOilVol;
	}





	public void setTotEstOilVol(Double totEstOilVol) {
		this.totEstOilVol = totEstOilVol;
	}





	public Double getTotEstGasVol() {
		return totEstGasVol;
	}





	public void setTotEstGasVol(Double totEstGasVol) {
		this.totEstGasVol = totEstGasVol;
	}





	public Double getAvgYearlyGas() {
		return avgYearlyGas;
	}





	public void setAvgYearlyGas(Double avgYearlyGas) {
		this.avgYearlyGas = avgYearlyGas;
	}





	public String getDayCount() {
		return dayCount;
	}





	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}





	public Date getRecordDate() {
		return recordDate;
	}





	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}





	public String getOilMeterName() {
		return oilMeterName;
	}





	public void setOilMeterName(String oilMeterName) {
		this.oilMeterName = oilMeterName;
	}





	public String getGasMeterName() {
		return gasMeterName;
	}





	public void setGasMeterName(String gasMeterName) {
		this.gasMeterName = gasMeterName;
	}





	public Integer getOilMeterMID() {
		return oilMeterMID;
	}





	public void setOilMeterMID(Integer oilMeterMID) {
		this.oilMeterMID = oilMeterMID;
	}





	public Integer getGasMeterMID() {
		return gasMeterMID;
	}





	public void setGasMeterMID(Integer gasMeterMID) {
		this.gasMeterMID = gasMeterMID;
	}





	public Double getDailyOilGasRatio() {
		return dailyOilGasRatio;
	}





	public void setDailyOilGasRatio(Double dailyOilGasRatio) {
		this.dailyOilGasRatio = dailyOilGasRatio;
	}





	public Double getYearlyOilGasRatio() {
		return yearlyOilGasRatio;
	}





	public void setYearlyOilGasRatio(Double yearlyOilGasRatio) {
		this.yearlyOilGasRatio = yearlyOilGasRatio;
	}





	@Override
	public String toString() {
		return "GasBlowByDto [muwiId=" + muwiId + ", wellName=" + wellName + ", merrickID=" + merrickID + ", estOilVol="
				+ estOilVol + ", estGasVol=" + estGasVol + ", totEstOilVol=" + totEstOilVol + ", totEstGasVol="
				+ totEstGasVol + ", avgYearlyGas=" + avgYearlyGas + ", dayCount=" + dayCount + ", recordDate="
				+ recordDate + ", oilMeterName=" + oilMeterName + ", gasMeterName=" + gasMeterName + ", oilMeterMID="
				+ oilMeterMID + ", gasMeterMID=" + gasMeterMID + ", dailyOilGasRatio=" + dailyOilGasRatio
				+ ", yearlyOilGasRatio=" + yearlyOilGasRatio + "]";
	}

}
