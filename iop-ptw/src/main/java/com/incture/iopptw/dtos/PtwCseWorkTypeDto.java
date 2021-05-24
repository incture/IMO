package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class PtwCseWorkTypeDto {
	private Integer permitNumber;
	private Integer tank;
	private Integer vessel;
	private Integer excavation;
	private Integer pit;
	private Integer tower;
	private String other;
	private String reasonForCSE;
}
