package com.incture.iopptw.dtos;

import java.util.Date;

import lombok.Data;
@Data
public class IOPPtwDto {
	private Integer permitNumber;
	private String ptwPermitNumber;
	private Byte isCwp;
	private Byte isHwp;
	private Byte isCse;
	private Date plannedDateTime;
	private String location;
	private String createdBy;
	private String contractorPerformingWork;
	private Date estimatedTimeOfCompletion;
	private String equipmentId;
	private String workOrderNumber;
	private String Status;
	private Byte isActive;
}
