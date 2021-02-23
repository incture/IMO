/**
 * 
 */
package com.murphy.taskmgmt.dto;

import java.util.Date;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

/**
 * @author Kamlesh.Choubey
 *
 */
public class SsdBypassActivityLogDto extends BaseDto {

	private String ssdBypassLogId;
	private String ssdBypassId;
	private String personResponsible;
	private Date bypassStatusReviewedAt;
	private Boolean isApprovalObtained;
	private String operatorType;
	private String personId;
	private String activityType;

	/**
	 * @return the ssdBypassLogId
	 */
	public String getSsdBypassLogId() {
		return ssdBypassLogId;
	}

	/**
	 * @param ssdBypassLogId
	 *            the ssdBypassLogId to set
	 */
	public void setSsdBypassLogId(String ssdBypassLogId) {
		this.ssdBypassLogId = ssdBypassLogId;
	}

	/**
	 * @return the ssdBypassId
	 */
	public String getSsdBypassId() {
		return ssdBypassId;
	}

	/**
	 * @param ssdBypassId
	 *            the ssdBypassId to set
	 */
	public void setSsdBypassId(String ssdBypassId) {
		this.ssdBypassId = ssdBypassId;
	}

	/**
	 * @return the personResponsible
	 */
	public String getPersonResponsible() {
		return personResponsible;
	}

	/**
	 * @param personResponsible
	 *            the personResponsible to set
	 */
	public void setPersonResponsible(String personResponsible) {
		this.personResponsible = personResponsible;
	}

	/**
	 * @return the bypassStatusReviewedAt
	 */
	public Date getBypassStatusReviewedAt() {
		return bypassStatusReviewedAt;
	}

	/**
	 * @param bypassStatusReviewedAt
	 *            the bypassStatusReviewedAt to set
	 */
	public void setBypassStatusReviewedAt(Date bypassStatusReviewedAt) {
		this.bypassStatusReviewedAt = bypassStatusReviewedAt;
	}

	/**
	 * @return the isApprovalObtained
	 */
	public Boolean getIsApprovalObtained() {
		return isApprovalObtained;
	}

	/**
	 * @param isApprovalObtained
	 *            the isApprovalObtained to set
	 */
	public void setIsApprovalObtained(Boolean isApprovalObtained) {
		this.isApprovalObtained = isApprovalObtained;
	}

	/**
	 * @return the operatorType
	 */
	public String getOperatorType() {
		return operatorType;
	}

	/**
	 * @param operatorType
	 *            the operatorType to set
	 */
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the personId
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * @param activityType
	 *            the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * @param personId
	 *            the personId to set
	 */
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
