package com.murphy.taskmgmt.dto;



import java.math.BigDecimal;
import java.util.List;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class DowntimeResponseDto extends BaseDto{

private List<DowntimeCapturedDto> dtoList;
private ResponseMessage message;
private BigDecimal totalCount;
private BigDecimal pageCount;
private List<LocationHistoryRolledUpDto> itemList ;

public List<LocationHistoryRolledUpDto> getItemList() {
	return itemList;
}

public void setItemList(List<LocationHistoryRolledUpDto> itemList) {
	this.itemList = itemList;
}

@Override
public String toString() {
	return "DowntimeResponseDto [dtoList=" + dtoList + ", message=" + message + ", totalCount=" + totalCount
			+ ", pageCount=" + pageCount + ", itemList=" + itemList + "]";
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


public List<DowntimeCapturedDto> getDtoList() {
	return dtoList;
}

public void setDtoList(List<DowntimeCapturedDto> dtoList) {
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
