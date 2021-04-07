package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwApprovalDto {
	private int serialNo;
	private int permitNumber;
	private int isCwp;
	private int isHwp;
	private int isCse;
	private int isWorkSafeToPerform;
	private String preJobWalkThroughBy;
	private String approvedBy;
	private Date approvalDate;
	private int controlBoardDistribution;
	private int workSiteDistribution;
	private int simopsDistribution;
	private String otherDistribution;
	private String picName;
	private Date picDate;
	private String superItendentName;
	private Date superItendentDate;
}
