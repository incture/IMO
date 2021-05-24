package com.incture.iopptw.dtos;

import lombok.Data;

@Data
public class JsaHazardsPressurizedDto {
	private Integer permitNumber;
	private Integer presurizedEquipment;
	private Integer performIsolation;
	private Integer depressurizeDrain;
	private Integer relieveTrappedPressure;
	private Integer doNotWorkInLineOfFire;
	private Integer anticipateResidual;
	private Integer secureAllHoses;
}
