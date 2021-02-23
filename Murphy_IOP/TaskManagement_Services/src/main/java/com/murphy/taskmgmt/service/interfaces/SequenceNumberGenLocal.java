package com.murphy.taskmgmt.service.interfaces;

public interface SequenceNumberGenLocal {
	public String getNextSeqNumber(String referenceCode, Integer noOfDigits);
}
