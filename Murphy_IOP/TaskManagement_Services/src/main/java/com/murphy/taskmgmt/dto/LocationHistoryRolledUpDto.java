package com.murphy.taskmgmt.dto;

import java.math.BigDecimal;

public class LocationHistoryRolledUpDto {
	String status;
	BigDecimal count;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getCount() {
		return count;
	}
	public void setCount(BigDecimal count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "LocHistoryRolledUpDto [status=" + status + ", count=" + count + "]";
	}
}
