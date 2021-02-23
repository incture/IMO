package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JSA_HIERARCHY")
public class LocationHierarchyDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MUWI", length = 25)
	private String muwi=UUID.randomUUID().toString().replaceAll("-", "");
	
	@Column(name = "BUSINESSENTITY", length = 25)
	private String businessEntity;

	@Column(name = "BUISNESSUNIT", length = 25)
	private String businessUnit;
	
	@Column(name = "FIELD", length = 25)
	private String field;
	
	@Column(name = "FACILITY", length = 25)
	private String facility;
	
	@Column(name = "WELLPAD" ,length=50)
	private String wellpad;
	
	@Column(name = "WELL", length = 25)
	private String well;
	
//	@Column(name = "LONGITUDE")
//	private BigDecimal longitude;
//	
//	@Column(name = "LATITUDE")
//	private BigDecimal latitude;

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

	public String getMuwi() {
		return muwi;
	}

	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}


//	public BigDecimal getLongitude() {
//		return longitude;
//	}
//
//	public void setLongitude(BigDecimal longitude) {
//		this.longitude = longitude;
//	}
//
//	public BigDecimal getLatitude() {
//		return latitude;
//	}
//
//	public void setLatitude(BigDecimal latitude) {
//		this.latitude = latitude;
//	}

	@Override
	public String toString() {
		return "LocationHierarchyDo [muwi=" + muwi + ", businessEntity=" + businessEntity + ", businessUnit="
				+ businessUnit + ", field=" + field + ", facility=" + facility + ", wellpad=" + wellpad + ", well="
				+ well + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return muwi;
	}
	

	
}
