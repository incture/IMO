package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsVoltage {
	private Integer permitNumber;
	private Integer highVoltage;
	private Integer restrictAccess;
	private Integer dischargeEquipment;
	private Integer observeSafeWorkDistance;
	private Integer useFlashBurn;
	private Integer useInsulatedGloves;
}
