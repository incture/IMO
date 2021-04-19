package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PermitsByLocRecordDto {
	private String jsaPermitNumber;
	private String ptwPermitNumber;
	private String createdBy;
	private int isCwp;
	private int isHwp;
	private int isCse;
	private Date createdDate;
	private String facilityorsite;
	private String permitNumber;
	private Date lastUpdatedDate;
	private String status;
}
