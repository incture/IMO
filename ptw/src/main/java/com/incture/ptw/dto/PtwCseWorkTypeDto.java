package com.incture.ptw.dto;

import lombok.Data;

@Data
public class PtwCseWorkTypeDto {
	private Integer permitNumber;
	private Integer tank;
	private Integer vessel;
	private Integer excavation;
	private Integer pit;
	private Integer tower;
	private String Other;
	private String reasonForCSE;
}
