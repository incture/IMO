package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwHeaderDto {
	private Integer permitNumber;
	private String ptwPermitNumber;
	private Integer isCwp;
	private Integer isHwp;
	private Integer isCse;
	private Date plannedDateTime;
	private String location;
	private String createdBy;
	private String contractorPerformingWork;
	private Date estimatedTimeOfCompletion;
	private String equipmentId;
	private String workOrderNumber;
	private String status;
}
