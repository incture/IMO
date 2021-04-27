package com.incture.ptw.dto;

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
	private Integer erectingOrDismantlingScaffodling;
	private Integer breakingContainmentOfClosedOperatingSystem;
	private Integer workingInCloseToHazardousEnergy;
	private Integer removalOfIdelEquipemntForRepair;
	private Integer higherRiskElectricalWork;
	private String otherTypeOfWork;
	private String descriptionOfWorkToBePerformed;

}
