package com.murphy.integration.dto;

public class FlareDowntimeCaptureDto {

	// private static final Logger logger = LoggerFactory.getLogger(DowntimeCaptureDao.class);
	
	private int merrickId;
	private float flareVolume;
	private String flareCode;
	private String recordDate;
	//For incident INC0077904
	private String productionDate;
	
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
		return "FlareDowntimeCaptureDto [merrickId=" + merrickId + ", flareVolume=" + flareVolume + ", flareCode=" + flareCode
				+ ", recordDate=" + recordDate + "]"; 
	}
	/**
	 * @return the productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}
	/**
	 * @param productionDate the productionDate to set
	 */
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
}
