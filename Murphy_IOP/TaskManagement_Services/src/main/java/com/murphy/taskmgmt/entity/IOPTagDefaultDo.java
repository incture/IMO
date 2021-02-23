package com.murphy.taskmgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IOP_TAG_DEFAULT")
public class IOPTagDefaultDo implements BaseDo,Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9117513356586164258L;

	@Id
	@Column(name = "TAG_ID")
	private int tagId;
	
	@Column(name = "DISPLAY_NAME")
	private String displayName;
	
	@Column(name = "TAG_NAME")
	private String tagName;
	
	@Column(name = "AGGREGATION")
	private String aggregation ;
	
	@Column(name = "UNIT")
	private String unit ;

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "IOPTagDefaultDo [tagId=" + tagId + ", displayName=" + displayName + ", tagName=" + tagName
				+ ", aggregation=" + aggregation + ", unit=" + unit + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return tagId;
	}
	
	
	
}
