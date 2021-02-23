package com.murphy.taskmgmt.util;

public enum TaskEnumConstants {

	
	Inquiry ("INQ_Atv"),
	Investigation ("INV_Atv"),
	Dispatch ("DIS_Atv");

	 private String statusCode;
	 
	 

    TaskEnumConstants(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	 
	 
	
}