package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class LocationHierarchyDto extends BaseDto {

	private String muwi;
	private String businessEntity;
	private String businessUnit;
	private String field;
	private String facility;
	private String wellpad;
	private String well;
	private BigDecimal longValue;
	private BigDecimal latValue;
	private String childExist;
	private String location;
	private String locationText;
	private String locationType;
	private String tier;
	private String locCode;
	
	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	public String getBusinessEntity() {
		return businessEntity;
	}

	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getWellpad() {
		return wellpad;
	}

	public void setWellpad(String wellpad) {
		this.wellpad = wellpad;
	}

	public String getWell() {
		return well;
	}

	public void setWell(String well) {
		this.well = well;
	}

	public BigDecimal getLongValue() {
		return longValue;
	}

	public void setLongValue(BigDecimal longValue) {
		this.longValue = longValue;
	}

	public BigDecimal getLatValue() {
		return latValue;
	}

	public void setLatValue(BigDecimal latValue) {
		this.latValue = latValue;
	}

	public String getChildExist() {
		return childExist;
	}

	public void setChildExist(String childExist) {
		this.childExist = childExist;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "LocationHierarchyDto [muwi=" + muwi + ", businessEntity=" + businessEntity + ", businessUnit="
				+ businessUnit + ", field=" + field + ", facility=" + facility + ", wellpad=" + wellpad + ", well="
				+ well + ", longValue=" + longValue + ", latValue=" + latValue + ", childExist=" + childExist
				+ ", location=" + location + ", locationText=" + locationText + ", locationType=" + locationType
				+ ", tier=" + tier + ", locCode=" + locCode + "]";
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

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

}
