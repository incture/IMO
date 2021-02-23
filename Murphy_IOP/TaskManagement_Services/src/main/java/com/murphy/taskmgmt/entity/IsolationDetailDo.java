package com.murphy.taskmgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EI_ISOLATION_DETAIL")
public class IsolationDetailDo implements BaseDo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 40)
	private String id;
	
	@Column(name = "FORM_ID")
	private String formId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "IS_EI_STORED")
	private boolean isEIStored;
	
	@Column(name = "IS_EQUIP_TESTED")
	private boolean isEquipTested;
	
	@Column(name = "IS_DELETED")
	private boolean isDeleted;
	
	@Column(name = "ISOLATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date isolationDate;
	
	@Column(name = "REINSTATEMENT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reinstatement;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEIStored() {
		return isEIStored;
	}

	public void setEIStored(boolean isEIStored) {
		this.isEIStored = isEIStored;
	}

	public boolean isEquipTested() {
		return isEquipTested;
	}

	public void setEquipTested(boolean isEquipTested) {
		this.isEquipTested = isEquipTested;
	}

	public Date getIsolationDate() {
		return isolationDate;
	}

	public void setIsolationDate(Date isolationDate) {
		this.isolationDate = isolationDate;
	}

	public Date getReinstatement() {
		return reinstatement;
	}

	public void setReinstatement(Date reinstatement) {
		this.reinstatement = reinstatement;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
