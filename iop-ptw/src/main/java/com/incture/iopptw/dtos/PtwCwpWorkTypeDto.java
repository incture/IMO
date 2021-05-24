package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class PtwCwpWorkTypeDto {
	private Integer permitNumber;
	private Integer criticalOrComplexLift;
	private Integer craneOrLiftingDevice;
	private Integer groundDisturbanceOrExcavation;
	private Integer handlingHazardousChemicals;
	private Integer workingAtHeight;
	private Integer paintingOrBlasting;
	private Integer workingOnPressurizedSystems;
	private Integer erectingOrDismantlingScaffolding;
	private Integer breakingContainmentOfClosedOperatingSystem;
	private Integer workingInCloseToHazardousEnergy;
	private Integer removalOfIdleEquipmentForRepair;
	private Integer higherRiskElectricalWork;
	private String otherTypeOfWork;
	private String descriptionOfWorkToBePerformed;

}
