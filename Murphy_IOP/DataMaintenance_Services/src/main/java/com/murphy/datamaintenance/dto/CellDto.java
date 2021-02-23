package com.murphy.datamaintenance.dto;

public class CellDto {
	private String columnName;
	private String columnType;
	private Object columnValue;
	private Integer cellIndex;
	private Boolean isPrimaryKey;
	private Integer columnLength;
	private Boolean isNotNullable;
	
	public Boolean getIsNotNullable() {
		return isNotNullable;
	}
	public void setIsNotNullable(Boolean isNotNullable) {
		this.isNotNullable = isNotNullable;
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
	public Object getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	public Integer getCellIndex() {
		return cellIndex;
	}
	public void setCellIndex(Integer cellIndex) {
		this.cellIndex = cellIndex;
	}
	public Boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(Boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Integer getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}
}
