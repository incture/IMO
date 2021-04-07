package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaheaderDto {
	private int permitNumber;
	private String jsaPermitNumber;
	private int hasCwp;
	private int hasHwp;
	private int hasCse;
	private String taskDescription;
	private String identifyMostSeriousPotentialInjury;
	private int isActive;
	private String status;

}
