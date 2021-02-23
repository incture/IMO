package com.murphy.integration.dto;

import java.math.BigDecimal;

public class PROVEUIResponseDto {
	
	UIResponseDto uiResponseDto;
	private int totalCount;
	private int pageCount;
	
	
	public UIResponseDto getUiResponseDto() {
		return uiResponseDto;
	}
	public void setUiResponseDto(UIResponseDto uiResponseDto) {
		this.uiResponseDto = uiResponseDto;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	@Override
	public String toString() {
		return "PROVEUIResponseDto [uiResponseDto=" + uiResponseDto + ", totalCount=" + totalCount + ", pageCount="
				+ pageCount + "]";
	}

}
