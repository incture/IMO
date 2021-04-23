package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;
@Data
public class JsaReviewDto {
	private Integer permitNumber;
	private String createdBy;
	private String approvedBy;
	private Date approvedDate;
	private String lastUpdatedBy;
	private Date lastUpdatedDate;
	private Date createdDate;
	
	

}
