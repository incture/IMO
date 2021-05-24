package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class JsaHazardsMobileDto {
	private Integer permitNumber;
	private Integer mobileEquipment;
	private Integer assessEquipmentCondition;
	private Integer controlAccess;
	private Integer monitorProximity;
	private Integer manageOverheadHazards;
	private Integer adhereToRules;
}
