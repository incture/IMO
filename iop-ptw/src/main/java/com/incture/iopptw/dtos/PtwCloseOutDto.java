package com.incture.iopptw.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class PtwCloseOutDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private String picName;
	private Integer workCompleted;
	private String closedBy;
	private Date closedDate;
	private String workStatusComment;
}
