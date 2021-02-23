package com.murphy.taskmgmt.dto;

import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.util.EnOperation;

public class SequenceNumberDto extends BaseDto {
	private String seqCategory;
	private int nextAvailableNum;
	
	public String getSeqCategory() {
		return seqCategory;
	}
	public void setSeqCategory(String seqCategory) {
		this.seqCategory = seqCategory;
	}
	public int getNextAvailableNum() {
		return nextAvailableNum;
	}
	public void setNextAvailableNum(int nextAvailableNum) {
		this.nextAvailableNum = nextAvailableNum;
	}
	
	@Override
	public String toString() {
		return "SequenceNumberDto [seqCategory=" + seqCategory + ", nextAvailableNum=" + nextAvailableNum + "]";
	}
	
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
	
}
