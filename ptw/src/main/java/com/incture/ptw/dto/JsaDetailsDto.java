package com.incture.ptw.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class JsaDetailsDto {
	private Date approvedDate;
	private String createdBy;
	private Date createdDate;
	private List<String> facilityOrSite;
	private String jsaPermitNumber;
	private Date lastUpdatedDate;
	private int permitNumber;
	private List<String> ptwPermitNumber;
	private String status;
	private String taskDescription;
}
