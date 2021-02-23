package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EI_REASON")
public class EIReasonDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "REASON")
	private String reason;

	@Column(name="ACTIVE_FLAG",length=20)
	private String activeFlag;
	
	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}
	
}
