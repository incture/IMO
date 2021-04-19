package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class JsaRecord {
	private Date approvedDate;
	private String createdBy;
	private Date createdDate;
	private String facilityOrSite;
	private String jsaPermitNumber;
	private Date lastUpdatedDate;
	private String permitNumber;
	private String ptwPermitNumber;
	private String status;
	private String taskDescription;
}
