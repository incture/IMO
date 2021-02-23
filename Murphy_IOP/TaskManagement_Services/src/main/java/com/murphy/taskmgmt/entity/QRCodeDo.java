package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QRCODE_EQUIPMENT_DETAIL")
public class QRCodeDo implements BaseDo, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "QR_CODE", length = 32)
	private String qrCode =UUID.randomUUID().toString().replaceAll("-", "");
	
	@Column(name = "LOCATION_CODE", length = 50)
	private String locationCode;
	
	@Column(name = "LOCATION_TEXT", length = 100)
	private String locationText;
	
	@Column(name = "EQUIPMENT_CODE", length = 50)
	private String equipmentCode;
	
	@Column(name = "EQUIPMENT_TEXT", length = 100)
	private String equipmentText;
	
	@Column(name = "EQUIPMENT_TYPE", length = 50)
	private String equipmentType;
	
	@Column(name = "CANARY_TAG", length = 1000)
	private String canaryTag;
	
	
	public String getQrCode() {
		return qrCode;
	}



	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}



	public String getLocationCode() {
		return locationCode;
	}



	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}



	public String getLocationText() {
		return locationText;
	}



	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}



	public String getEquipmentCode() {
		return equipmentCode;
	}



	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}



	public String getEquipmentText() {
		return equipmentText;
	}



	public void setEquipmentText(String equipmentText) {
		this.equipmentText = equipmentText;
	}



	public String getEquipmentType() {
		return equipmentType;
	}



	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}



	public String getCanaryTag() {
		return canaryTag;
	}



	public void setCanaryTag(String canaryTag) {
		this.canaryTag = canaryTag;
	}



	
	
	
	@Override
	public String toString() {
		return "QRCodeDo [qrCode=" + qrCode + ", locationCode=" + locationCode + ", locationText=" + locationText
				+ ", equipmentCode=" + equipmentCode + ", equipmentText=" + equipmentText + ", equipmentType="
				+ equipmentType + ", canaryTag=" + canaryTag + "]";
	}



	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
