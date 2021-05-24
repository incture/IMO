package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class JsaHazardsSimultaneousDto {
	private Integer permitNumber;
	private Integer simultaneousOperations;
	private Integer followSimopsMatrix;
	private Integer mocRequiredFor;
	private Integer interfaceBetweenGroups;
	private Integer useBarriersAnd;
	private Integer havePermitSigned;
}
