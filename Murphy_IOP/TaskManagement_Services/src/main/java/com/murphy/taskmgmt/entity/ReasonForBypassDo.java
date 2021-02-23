package com.murphy.taskmgmt.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REASON_FOR_BYPASS")
public class ReasonForBypassDo implements BaseDo {

	/*@Id
	@Column(name = "REASON_FOR_BYPASS_ID", length = 50)
	private String reasonForBypassId = UUID.randomUUID().toString().replaceAll("-", "");;*/

	@Id
	@Column(name = "REASON_FOR_BYPASS", length = 500)
	private String reasonForBypass;
	
	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	
	/**
	 * @return the reasonForBypass
	 */
	public String getReasonForBypass() {
		return reasonForBypass;
	}

	/**
	 * @param reasonForBypass
	 *            the reasonForBypass to set
	 */
	public void setReasonForBypass(String reasonForBypass) {
		this.reasonForBypass = reasonForBypass;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return reasonForBypass;
	}

}
