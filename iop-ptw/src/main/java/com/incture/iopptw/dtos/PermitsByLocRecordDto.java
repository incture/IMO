package com.incture.iopptw.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class PermitsByLocRecordDto {
	private String jsaPermitNumber;
	private String ptwPermitNumber;
	private String createdBy;
	private Byte isCwp;
	private Byte isHwp;
	private Byte isCse;
	private Date createdDate;
	private String facilityorsite;
	private String permitNumber;
	private Date lastUpdatedDate;
	private String status;
}
