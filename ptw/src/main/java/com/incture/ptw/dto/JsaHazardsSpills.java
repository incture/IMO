package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsSpills {
	private Integer permitNumber;
	private Integer potentialSpills;
	private Integer drainEquipment;
	private Integer connectionSinGoodCondition;
	private Integer spillContainmentEquipment;
	private Integer haveSpillCleanUpMaterials;
	private Integer restRainHosesWhenNotInUse;
}
