package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwCloseOut1Dto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private String picName;
	private Integer workCompleted;
	private String closedBy;
	private Date closedDate;
	private String workStatusComments;
	private String approvedBy;
}
