package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwTestResultsDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCwp;
	private Integer isHwp;
	private Integer isCse;
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