package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;
@Data
public class JsaReviewDto {
	int permitNumber;
	String createdBy;
	String approvedBy;
	Date approvedDate;
	String lastUpdatedBy;
	Date lastUpdatedDate;
	Date createdDate;
	
	

}
