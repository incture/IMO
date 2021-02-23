package com.murphy.taskmgmt.dto;

import java.util.Date;

public class DropDownDataDto {
	
	private String classification;
	private String subclassification;
	private String itemId;
	private String valueId;
	private int insertRows;
	private Date deleteAt;
	public int getInsertRows() {
		return insertRows;
	}
	public void setInsertRows(int insertRows) {
		this.insertRows = insertRows;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubclassification() {
		return subclassification;
	}
	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getValueId() {
		return valueId;
	}
	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	
	public Date getDeleteAt() {
		return deleteAt;
	}
	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}
	@Override
	public String toString() {
		return "DropDownDataDto [classification=" + classification + ", subclassification=" + subclassification
				+ ", itemId=" + itemId + ", valueId=" + valueId + ", insertRows=" + insertRows + ", deleteAt="
				+ deleteAt + "]";
	}
	
	
	

	

}
