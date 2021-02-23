package com.murphy.datamaintenance.dto;

import java.util.List;

public class ExcelFormatDto {
	private String sheetName;
	private List<String> sheetHeaders;
	private List<TableRowDto> tableRowDtosList;

	public List<TableRowDto> getTableRowDtosList() {
		return tableRowDtosList;
	}

	public void setTableRowDtosList(List<TableRowDto> tableRowDtosList) {
		this.tableRowDtosList = tableRowDtosList;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<String> getSheetHeaders() {
		return sheetHeaders;
	}

	public void setSheetHeaders(List<String> sheetHeaders) {
		this.sheetHeaders = sheetHeaders;
	}

	

}
