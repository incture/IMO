package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwTestResultsDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private String preStartOrWorkTest;
	private Float oxygenPercentage;
	private String toxicType;
	private Float toxicResult;
	private String flammableGas;
	private String othersType;
	private Float othersResult;
	private Date date;
	private Date time;
}