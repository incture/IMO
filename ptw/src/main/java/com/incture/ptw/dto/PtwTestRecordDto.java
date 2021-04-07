package com.incture.ptw.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PtwTestRecordDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCwp;
	private Integer isHwp;
	private Integer isCse;
	private String detectorUsed;
	private Date dateOfLastCalibration;
	private String testingFrequency;
	private Integer continuousGasMonitoring;
	private Integer priorToWorkCommencing;
	private Integer eachWorkPeriod;
	private Integer everyHour;
	private String gasTester;
	private String gasTesterComments;
	private String areaToBeTested;
	private String deviceSerialNo;
	private Integer iso2;
	private Integer islels;
	private Integer ish2s;
	private String other;
}
