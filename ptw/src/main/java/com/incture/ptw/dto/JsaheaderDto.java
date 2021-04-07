package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaheaderDto {
	int permitNumber;
	String jsaPermitNumber;
	int hasCwp;
	int hasHwp;
	int hasCse;
	String taskDescription;
	String identifyMostSeriousPotentialInjury;
	int isActive;
	String status;

}
