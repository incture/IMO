package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class ReasonForBypassDto extends BaseDto {

	private String reasonForBypass;
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
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
