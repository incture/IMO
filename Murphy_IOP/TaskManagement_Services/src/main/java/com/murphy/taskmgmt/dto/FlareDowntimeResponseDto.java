package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class FlareDowntimeResponseDto extends BaseDto {

	private List<FlareDowntimeDto> dtoList;
	private ResponseMessage message;
	private BigDecimal totalCount;
	private BigDecimal pageCount;
	private List<LocationHistoryRolledUpDto> itemList ;
	
	public BigDecimal getPageCount() {
		return pageCount;
	}

	public List<LocationHistoryRolledUpDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<LocationHistoryRolledUpDto> itemList) {
		this.itemList = itemList;
	}

	public void setPageCount(BigDecimal pageCount) {
		this.pageCount = pageCount;
	}

	public BigDecimal getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "FlareDowntimeResponseDto [dtoList=" + dtoList + ", message=" + message + ", totalCount=" + totalCount
				+ ", pageCount=" + pageCount + ", itemList=" + itemList + "]";
	}

	public List<FlareDowntimeDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<FlareDowntimeDto> dtoList) {
		this.dtoList = dtoList;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
