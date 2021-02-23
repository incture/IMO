package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: ProcessTemplateDo
 *
 */
@Entity
@Table(name = "TM_PROC_TEMPS")
public class ProcessTemplateDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public ProcessTemplateDo() {
		super();
	}

	private static final long serialVersionUID = -7341365853980611944L;

	@Id
	@Column(name = "PROC_TEMP_ID", length = 32)
	private String processTemplateId;	

	@Column(name = "PROC_NAME", length = 200)
	private String processName;

	@Column(name = "CREATED_BY", length = 250)
	private String createdBy;

	@Column(name = "CREATED_BY_DISP", length = 250)
	private String createdByDisplay;

	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "SLA", length = 20)
	private String sla;

	@Column(name = "TEMP_TYPE", length = 50)
	private String tempType;

	@Column(name = "LAST_UPDATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;



	public String getProcessTemplateId() {
		return processTemplateId;
	}


	public void setProcessTemplateId(String processTemplateId) {
		this.processTemplateId = processTemplateId;
	}

	public String getProcessName() {
		return processName;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedByDisplay() {
		return createdByDisplay;
	}

	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getSla() {
		return sla;
	}


	public void setSla(String sla) {
		this.sla = sla;
	}


	public String getTempType() {
		return tempType;
	}


	public void setTempType(String tempType) {
		this.tempType = tempType;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}


	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


	@Override
	public String toString() {
		return "ProcessTemplateDo [processTemplateId=" + processTemplateId + ", processName=" + processName
				+ ", createdBy=" + createdBy + ", createdByDisplay=" + createdByDisplay + ", createdAt=" + createdAt
				+ ", sla=" + sla + ", tempType=" + tempType + ", lastUpdated=" + lastUpdated + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return processTemplateId;
	}



}
