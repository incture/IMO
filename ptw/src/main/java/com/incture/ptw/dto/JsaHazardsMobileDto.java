package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsMobileDto {
	private Integer permitNumber;
	private Integer mobileEquipment;
	private Integer assessEquipmentCondition;
	private Integer controlAccess;
	private Integer monitorProximity;
	private Integer manageOverHeadHazards;
	private Integer adhereToRules;
}
