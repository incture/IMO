package com.murphy.datamaintenance.dto;

import java.util.List;

public class TableRowDto {
	private int rowIndex;
	private List<CellDto> cellDtoList;
	

	public List<CellDto> getCellDtoList() {
		return cellDtoList;
	}

	public void setCellDtoList(List<CellDto> cellDtoList) {
		this.cellDtoList = cellDtoList;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	

}
