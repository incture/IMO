package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwTestRecordDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private String detectorUsed;
	private Date DateOfLastCalibration;
	private String testingFrequency;
	private Integer continuousGasMonitoring;
	private Integer priorToWorkCommencing;
	private Integer eachWorkPeriod;
	private Integer everyHour;
	private String gasTester;
	private String gasTesterComments;
	private String areaTobeTested;
	private String deviceSerialNo;
	private Integer isO2;
	private Integer isLELs;
	private Integer isH2S;
	private String Other;
}
