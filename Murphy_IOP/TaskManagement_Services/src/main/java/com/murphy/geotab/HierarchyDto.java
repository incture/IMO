package com.murphy.geotab;

/**
 * @author INC00718
 *
 */
public class HierarchyDto {

	private String muwi;
	private String businessEntity;
	private String businessUnit;
	private String field;
	private String facility;
	private String wellpad;
	private String well;
	private Double latitude;
	private Double longitude;

	/**
	 * @return the muwi
	 */
	public String getMuwi() {
		return muwi;
	}

	/**
	 * @param muwi
	 *            the muwi to set
	 */
	public void setMuwi(String muwi) {
		this.muwi = muwi;
	}

	/**
	 * @return the businessEntity
	 */
	public String getBusinessEntity() {
		return businessEntity;
	}

	/**
	 * @param businessEntity
	 *            the businessEntity to set
	 */
	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}

	/**
	 * @return the businessUnit
	 */
	public String getBusinessUnit() {
		return businessUnit;
	}

	/**
	 * @param businessUnit
	 *            the businessUnit to set
	 */
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the facility
	 */
	public String getFacility() {
		return facility;
	}

	/**
	 * @param facility
	 *            the facility to set
	 */
	public void setFacility(String facility) {
		this.facility = facility;
	}

	/**
	 * @return the wellpad
	 */
	public String getWellpad() {
		return wellpad;
	}

	/**
	 * @param wellpad
	 *            the wellpad to set
	 */
	public void setWellpad(String wellpad) {
		this.wellpad = wellpad;
	}

	/**
	 * @return the well
	 */
	public String getWell() {
		return well;
	}

	/**
	 * @param well
	 *            the well to set
	 */
	public void setWell(String well) {
		this.well = well;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HierarchyDto [muwi=" + muwi + ", businessEntity=" + businessEntity + ", businessUnit=" + businessUnit
				+ ", field=" + field + ", facility=" + facility + ", wellpad=" + wellpad + ", well=" + well
				+ ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

}
