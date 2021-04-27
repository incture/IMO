package com.incture.ptw.dto;

import lombok.Data;

@Data
public class PtwRequiredDocumentDto {
	private Integer serialNo;
	private Integer permitNumber;
	private Integer isCWP;
	private Integer isHWP;
	private Integer isCSE;
	private Integer atmosphericTestRecord;
	private Integer loto;
	private Integer procedure;
	private Integer pAndIdOrDrawing;
	private String certificate;
	private Integer temporaryDefeat;
	private Integer rescuePlan;
	private Integer sds;
	private String otherWorkPermitDocs;
	private Integer fireWatchChecklist;
	private Integer liftPlan;
	private Integer simopDeviation;
	private Integer safeWorkPractice;
}
