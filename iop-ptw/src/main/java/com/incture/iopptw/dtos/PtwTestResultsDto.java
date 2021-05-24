package com.incture.iopptw.dtos;

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
	private Double oxygenPercentage;
	private String toxicType;
	private Double toxicResult;
	private String flammableGas;
	private String othersType;
	private Double othersResult;
	private Date date;
	private String time;
}