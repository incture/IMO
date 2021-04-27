package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsVisibilityDto {
	private Integer permitNumber;
	private Integer poorLighting;
	private Integer provideAlternateLighting;
	private Integer waitUntilVisibilityImprove;
	private Integer deferUntilVisibilityImprove;
	private Integer knowDistanceFromPoles;
}
