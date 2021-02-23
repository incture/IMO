package com.murphy.taskmgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
public class DeviceDo implements BaseDo {

	@Id
	@Column(name = "DEVICE_NAME", length = 100)
	private String deviceName;

	@Column(name = "SEVERITY", length = 20)
	private String severity;

	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return deviceName;
	}

}
