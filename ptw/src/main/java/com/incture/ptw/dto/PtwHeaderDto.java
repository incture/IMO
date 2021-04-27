package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwHeaderDto {
	private Integer permitNumber;
	private String ptwPermitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private Date plannedDateTime;
	private String location;
	private String createdBy;
	private String contractorPerformingWork;
	private Date estimatedTimeOfCompletion;
	private String equipmentID;
	private String workOrderNumber;
	private String status;
}
