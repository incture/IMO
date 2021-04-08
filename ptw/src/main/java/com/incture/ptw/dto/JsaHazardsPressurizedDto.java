package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsPressurizedDto {
	private Integer permitNumber;
	private Integer presurizedEquipment;
	private Integer performIsolation;
	private Integer depressurizeDrain;
	private Integer relieveTrappedPressure;
	private Integer doNotWorkInlineOfFire;
	private Integer anticipateResidual;
	private Integer secureAllHoses;
}
