package com.incture.ptw.dto;

import lombok.Data;

@Data
public class PtwRequiredDocumentDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCwp;
	private Integer isHwp;
	private Integer isCse;
	private Integer atmosphericTestRecord;
	private Integer loto;
	private Integer procedure;
	private Integer pandidorDrwaing;
	private String certificate;
	private Integer temporaryDefeat;
	private Integer rescuePlan;
	private Integer sds;
	private String otherWorkPermitDocs;
	private Integer fireWatchCheckList;
	private Integer liftPlan;
	private Integer simopDeviation;
	private Integer safeWorkPractice;
}
