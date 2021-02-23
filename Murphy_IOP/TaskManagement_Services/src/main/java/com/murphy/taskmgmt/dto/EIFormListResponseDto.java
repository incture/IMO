package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

import com.murphy.taskmgmt.dto.ResponseMessage;

public class EIFormListResponseDto {
	private List<EIFormListDto> formList;
	private List<EIFormRolledUpDto> statusCountList;	
	private ResponseMessage responseMessage;
	private BigDecimal totalCount;
	private BigDecimal pageCount;
	
	
	public List<EIFormListDto> getFormList() {
		return formList;
	}
	public void setFormList(List<EIFormListDto> formList) {
		this.formList = formList;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
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
	public List<EIFormRolledUpDto> getStatusCountList() {
		return statusCountList;
	}
	public void setStatusCountList(List<EIFormRolledUpDto> statusCountList) {
		this.statusCountList = statusCountList;
	}
	
}
