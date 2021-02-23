package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DT_WELL_CHILD_CODE")
public class DowntimeWellChildCodeDo implements Serializable, BaseDo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "CHILD_CODE", length = 100)
	private String childCode;
	@Column(name = "CHILD_CODE_DESCRIPTION", length = 200)
	private String childCodeDescription;
	@Column(name = "PARENT_CODE", length = 100)
	private String parentCode;
	@Column(name = "ACTIVE_FLAG", length = 20)
	private String activeFlag;

	public String getChildCode() {
		return childCode;
	}

	public void setChildCode(String childCode) {
		this.childCode = childCode;
	}

	public String getChildCodeDescription() {
		return childCodeDescription;
	}

	public void setChildCodeDescription(String childCodeDescription) {
		this.childCodeDescription = childCodeDescription;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
