package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaheaderDto {
	private Integer permitNumber;
	private String jsaPermitNumber;
	private Integer hasCwp;
	private Integer hasHwp;
	private Integer hasCse;
	private String taskDescription;
	private String identifyMostSeriousPotentialInjury;
	private Integer isActive;
	private String status;

}
