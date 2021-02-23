package com.murphy.taskmgmt.dto;

import java.math.BigInteger;

public class EIFormRolledUpDto {
	
	String status;
	BigInteger count;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	
}
