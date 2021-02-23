package com.murphy.taskmgmt.dto;

import java.util.HashMap;

public class QRCodeDetailsDto {
	private String qrCode;
	private String locationCode;
	private String locationText;
	private String equipmentId;
	private String equipmentText;
	private String equipmentType;
//	private String tagName;
//	private String pressure;
	private HashMap<String , String> tagPressureMap;
	private String[] canaryTag;
	
	
	private ResponseMessage responseMessage;
	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}
	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}
	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	/**
	 * @return the locationText
	 */
	public String getLocationText() {
		return locationText;
	}
	/**
	 * @param locationText the locationText to set
	 */
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	/**
	 * @return the equipmentId
	 */
	public String getEquipmentId() {
		return equipmentId;
	}
	/**
	 * @param equipmentId the equipmentId to set
	 */
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	/**
	 * @return the equipmentText
	 */
	public String getEquipmentText() {
		return equipmentText;
	}
	/**
	 * @param equipmentText the equipmentText to set
	 */
	public void setEquipmentText(String equipmentText) {
		this.equipmentText = equipmentText;
	}
	/**
	 * @return the equipmentType
	 */
	public String getEquipmentType() {
		return equipmentType;
	}
	/**
	 * @param equipmentType the equipmentType to set
	 */
	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	
	/**
	 * @return the responseMessage
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the canaryTag
	 */
	public String[] getCanaryTag() {
		return canaryTag;
	}
	/**
	 * @param canaryTag the canaryTag to set
	 */
	public void setCanaryTag(String[] canaryTag) {
		this.canaryTag = canaryTag;
	}
	/**
	 * @return the tagPressureMap
	 */
	public HashMap<String, String> getTagPressureMap() {
		return tagPressureMap;
	}
	/**
	 * @param tagPressureMap the tagPressureMap to set
	 */
	public void setTagPressureMap(HashMap<String, String> tagPressureMap) {
		this.tagPressureMap = tagPressureMap;
	}
	
	

}
