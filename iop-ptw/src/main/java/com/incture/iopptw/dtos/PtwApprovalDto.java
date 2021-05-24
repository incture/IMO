package com.incture.iopptw.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class PtwApprovalDto {
	private int serialNo;
	private int permitNumber;
	private int isCWP;
	private int isHWP;
	private int isCSE;
	private int isWorkSafeToPerform;
	private String preJobWalkthroughBy;
	private String approvedBy;
	private Date approvalDate;
	private int controlBoardDistribution;
	private int worksiteDistribution;
	private int simopsDistribution;
	private String otherDistribution;
	private String picName;
	private Date picDate;
	private String superitendentName;
	private Date superitendentDate;
}
