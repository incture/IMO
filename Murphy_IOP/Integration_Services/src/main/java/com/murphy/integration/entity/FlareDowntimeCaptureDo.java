package com.murphy.integration.entity;

public class FlareDowntimeCaptureDo {

	private int merrickId;
	private float flareVolume;
	private String flareCode;
	private String recordDate;
	public int getMerrickId() {
		return merrickId;
	}
	public void setMerrickId(int merrickId) {
		this.merrickId = merrickId;
	}
	public float getFlareVolume() {
		return flareVolume;
	}
	public void setFlareVolume(float flareVolume) {
		this.flareVolume = flareVolume;
	}
	public String getFlareCode() {
		return flareCode;
	}
	public void setFlareCode(String flareCode) {
		this.flareCode = flareCode;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	
	@Override
	public String toString() {
		return "FlareDowntimeCaptureDo [merrickId=" + merrickId + ", flareVolume=" + flareVolume + ", flareCode=" + flareCode
				+ ", recordDate=" + recordDate + "]"; 
	}
}
