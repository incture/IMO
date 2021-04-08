package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaStepsDto {
	private int serialNo;
	private int permitNumber;
	private String taskSteps;
	private String potentialHazards;
	private String hazardControls;
	private String personResponsible;
}
