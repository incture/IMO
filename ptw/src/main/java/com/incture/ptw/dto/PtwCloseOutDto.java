package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwCloseOutDto {
	private int serialNo;
	private int permitNumber;
	private int isCwp;
	private int isHwp;
	private int isCse;
	private String picName;
	private int workCompleted;
	private String closedBy;
	private Date closedDate;
	private String workStatusComment;
}
