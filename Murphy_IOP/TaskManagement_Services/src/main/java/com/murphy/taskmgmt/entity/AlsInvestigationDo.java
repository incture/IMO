package com.murphy.taskmgmt.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ALS_INVESTIGATION")
public class AlsInvestigationDo implements BaseDo {

	@Id
	@Column(name = "ALS_ID", length = 32)
	private String alsId=UUID.randomUUID().toString().replaceAll("-", "");

	@Column(name = "MUWI", length = 200)
	private String muwId;

	@Column(name = "DATE")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "REASON", length = 100)
	private String reason;
	
	@Column(name = "SOURCE", length = 100)
	private String source;
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAlsId() {
		return alsId;
	}

	public void setAlsId(String alsId) {
		this.alsId = alsId;
	}

	public String getMuwId() {
		return muwId;
	}

	public void setMuwId(String muwId) {
		this.muwId = muwId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public Object getPrimaryKey() {
		return alsId;
	}

	@Override
	public String toString() {
		return "AlsInvestigationDo [alsId=" + alsId + ", muwId=" + muwId + ", date=" + date + ", reason=" + reason
				+ ", source=" + source + "]";
	}

}
