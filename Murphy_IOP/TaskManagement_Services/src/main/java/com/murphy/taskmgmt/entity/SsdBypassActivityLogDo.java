/**
 * 
 */
package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Kamlesh.Choubey
 *
 */

@Entity
@Table(name = "SSD_BYPASS_ACTIVITY_LOG")
public class SsdBypassActivityLogDo implements BaseDo {

	@Id
	@Column(name = "SSD_BYPASS_LOG_ID", length = 100)
	private String ssdBypassLogId;

	@Column(name = "SSD_BYPASS_ID", length = 100)
	private String ssdBypassId;

	@Column(name = "PERSON_RESPONSIBLE", length = 100)
	private String personResponsible;

	@Column(name = "BYPASS_STATUS_REVIEWED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date bypassStatusReviewedAt = new Date();

	@Column(name = "IS_APPROVAL_OBTAINED")
	private Boolean isApprovalObtained;

	@Column(name = "OPERATOR_TYPE", length = 30)
	private String operatorType;

	@Column(name = "PERSON_ID", length = 50)
	private String personId;

	@Column(name = "ACTIVITY_TYPE", length = 30)
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

	/**
	 * @param bypassStatusReviewedAt
	 *            the bypassStatusReviewedAt to set
	 */
	public void setBypassStatusReviewedAt(Date bypassStatusReviewedAt) {
		this.bypassStatusReviewedAt = bypassStatusReviewedAt;
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
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
