package com.incture.ptw.dto;

import java.util.List;

import com.incture.ptw.dao.JsaReviewDao;

import lombok.Data;

@Data
public class GetJsaByPermitNumPayloadDto {
	private JsaheaderDto TOJSAHEADER;
	private JsaReviewDto TOJSAREVIEW;
	private JsaRiskAssesmentDto TOJSARISKASS;
	private JsappeDto TOJSE_PPE;
	private JsaHazardsPressurizedDto TOJSAHAZARDPRESS;
	private JsaHazardsVisibilityDto TOJSAHAZARDVISIBLE;
	private JsaHazardsPersonnelDto TOJSAHAZARDPERSON;
	private JsaHazardscseDto TOJSAHAZARDCSE;
	private JsaHazardsSimultaneousDto TOJSAHAZARDSIMULTAN;
	private JsaHazardsIgnitionDto TOJSAHAZARDIGNITION;
	private JsaHazardsSubstancesDto TOJSAHAZARDSUBS;
	private JsaHazardsSpillsDto TOJSAHAZARDSPILL;
	private JsaHazardsWeatherDto TOJSAHAZARDWEATHER;
	private JsaHazardsHighNoiseDto TOJSAHAZARDNOISE;
	private JsaHazardsDroppedDto TOJSAHAZARDDROPPED;
	private JsaHazardsLiftingDto TOJSAHAZARDLIFT;
	private JsaHazardsHeightsDto TOJSAHAZARDHEIGHT;
	private JsaHazardsElectricalDto TOJSAHAZARDELECTRICAL;
	private JsaHazardsMovingDto TOJSAHAZARDMOVING;
	private JsaHazardsManualDto TOJSAHAZARDMANUAL;
	private JsaHazardsToolsDto TOJSAHAZARDTOOLS;
	private JsaHazardsFallsDto TOJSAHAZARDFALLS;
	private JsaHazardsVoltageDto TOJSAHAZARDVOLTAGE;
	private JsaHazardsExcavationdDto TOJSAHAZARDEXCAVATION;
	private JsaHazardsMobileDto TOJSAHAZARDMOBILE;
	private List<JsaStepsDto> TOJSASTEPS;
	private List<JsaStopTriggerDto> TOJSASTOP;
	private List<JsaLocationDto> TOJSALOCATION;
	private List<PtwPeopleDto> TOPTWPEOPLE;
}
