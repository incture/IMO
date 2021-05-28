package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class DftGetReviewerPayloadDto {
	private String facilityCode;
	private String wellPadCode;
	private String fieldCode;
	private String wellCode;
	private String department;
}
