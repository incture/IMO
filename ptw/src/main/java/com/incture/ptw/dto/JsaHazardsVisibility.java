package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsVisibility {
	private Integer permitNumber;
	private Integer poorLighting;
	private Integer alternateLighting;
	private Integer waitUntilVisibilityImprove;
	private Integer deferUntilVisibility;
	private Integer knowDistanceFromPoles;
}
