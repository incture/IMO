package com.incture.ptw.dto;

import lombok.Data;

@Data
public class PtwHwpWorkTypeDto {
	private Integer permitNumber;
	private Integer cutting;
	private Integer wielding;
	private Integer electricalPoweredEquipment;
	private Integer grinding;
	private Integer abrasiveBlasting;
	private String otherTypeOfWork;
	private String descriptionOfWorkToBePerformed;
}
