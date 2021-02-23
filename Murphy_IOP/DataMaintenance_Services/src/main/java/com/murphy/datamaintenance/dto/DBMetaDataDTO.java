package com.murphy.datamaintenance.dto;

public class DBMetaDataDTO {
	private String columnName;
	private String columnType;
	private Boolean isPrimaryKey;
	private Integer columnLength;
	private Boolean isNotNullable;
	
	
	public Boolean getIsNotNullable() {
		return isNotNullable;
	}
	public void setIsNotNullable(Boolean isNotNullable) {
		this.isNotNullable = isNotNullable;
	}
	public Integer getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	

}
