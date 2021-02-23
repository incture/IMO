package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

public class LocationHierarchyResponseDto {

	
	
	private LocationHierarchyDto responseDto;
	private ResponseMessage message;
	// Added for location master data
	private List<LocationHierarchyDto> listLocationHierarchyDto;
	private BigDecimal totalCount;
	private BigDecimal pageCount;

	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	public BigDecimal getPageCount() {
		return pageCount;
	}
	public void setPageCount(BigDecimal pageCount) {
		this.pageCount = pageCount;
	}
	public LocationHierarchyDto getResponseDto() {
		return responseDto;
	}
	public void setResponseDto(LocationHierarchyDto responseDto) {
		this.responseDto = responseDto;
	}	
	
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "LocationHierarchyResponseDto [responseDto=" + responseDto + ", message=" + message
				+ ", listLocationHierarchyDto=" + listLocationHierarchyDto + ", totalCount=" + totalCount
				+ ", pageCount=" + pageCount + "]";
	}
	public List<LocationHierarchyDto> getListLocationHierarchyDto() {
		return listLocationHierarchyDto;
	}
	public void setListLocationHierarchyDto(List<LocationHierarchyDto> listLocationHierarchyDto) {
		this.listLocationHierarchyDto = listLocationHierarchyDto;
	}
	
	}
