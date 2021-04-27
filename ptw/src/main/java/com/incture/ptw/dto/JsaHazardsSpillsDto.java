package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsSpillsDto {
	private Integer permitNumber;
	private Integer potentialSpills;
	private Integer drainEquipment;
	private Integer connectionsInGoodCondition;
	private Integer spillContainmentEquipment;
	private Integer haveSpillCleanupMaterials;
	private Integer restrainHosesWhenNotInUse;
}
