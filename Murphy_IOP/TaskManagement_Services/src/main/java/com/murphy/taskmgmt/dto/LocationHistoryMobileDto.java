package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class LocationHistoryMobileDto extends BaseDto {
	
	List<CustomDowntimeResponseDto> custm;
	private ResponseMessage responseMessage;
	private BigDecimal totalCount;
	private BigDecimal pageCount;
	private List<LocationHistoryRolledUpDto> itemsList;
	

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

	@Override
	public String toString() {
		return "LocationHistoryMobileDto [custm=" + custm + ", responseMessage=" + responseMessage + ", totalCount="
				+ totalCount + ", pageCount=" + pageCount + ", itemsList=" + itemsList + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
    
	public List<CustomDowntimeResponseDto> getCustm() {
		return custm;
	}

	public void setCustm(List<CustomDowntimeResponseDto> custm) {
		this.custm = custm;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	public List<LocationHistoryRolledUpDto> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<LocationHistoryRolledUpDto> itemsList) {
		this.itemsList = itemsList;
	}
	

}
