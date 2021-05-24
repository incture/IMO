package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class JsaHazardsMovingDto {
	private Integer permitNumber;
	private Integer movingEquipment;
	private Integer confirmMachineryIntegrity;
	private Integer provideProtectiveBarriers;
	private Integer observerToMonitorProximityPeopleAndEquipment;
	private Integer lockOutEquipment;
	private Integer doNotWorkInLineOfFire;
}
