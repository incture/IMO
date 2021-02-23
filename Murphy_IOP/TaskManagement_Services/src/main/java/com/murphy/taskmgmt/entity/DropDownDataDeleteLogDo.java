package com.murphy.taskmgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_CLASS_SUBCLASS_DELETE_LOG")
public class DropDownDataDeleteLogDo implements BaseDo{
	
	@Id
	@Column(name="DELETE_ID",length=32)
	private String deleteId;
	
	@Column(name="CLASSIFICATION",length=200)
	private String classification;
	
	@Column(name="SUB_CLASSIFICATION",length=200)
	private String subClassification;
	
	
	@Column(name="DELETED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;

	public String getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getSubClassification() {
		return subClassification;
	}

	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Override
	public String toString() {
		return "DropDownDataDeleteLogDo [deleteId=" + deleteId + ", classification=" + classification
				+ ", subClassification=" + subClassification + ", deletedAt=" + deletedAt + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return deleteId;
	}
	
	

}
