package com.incture.ptw.dto;

import lombok.Data;

@Data
public class JsaHazardsWeatherDto {
	private Integer permitNumber;
	private Integer weather;
	private Integer controlsForSlipperySurface;
	private Integer heatBreak;
	private Integer coldHeaters;
	private Integer lightning;
}
