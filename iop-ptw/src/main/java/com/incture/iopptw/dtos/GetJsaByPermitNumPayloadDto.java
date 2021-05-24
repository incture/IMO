package com.incture.iopptw.dtos;

import java.util.List;

import lombok.Data;

@Data
public class GetJsaByPermitNumPayloadDto {
	private JsaheaderDto jsaheaderDto;
	private JsaReviewDto jsaReviewDto;
	private JsaRiskAssesmentDto jsaRiskAssesmentDto;
	private JsappeDto jsappeDto;
	private JsaHazardsPressurizedDto jsaHazardsPressurizedDto;
	private JsaHazardsVisibilityDto jsaHazardsVisibilityDto;
	private JsaHazardsPersonnelDto jsaHazardsPersonnelDto;
	private JsaHazardscseDto jsaHazardscseDto;
	private JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto;
	private JsaHazardsIgnitionDto jsaHazardsIgnitionDto;
	private JsaHazardsSubstancesDto jsaHazardsSubstancesDto;
	private JsaHazardsSpillsDto jsaHazardsSpillsDto;
	private JsaHazardsWeatherDto jsaHazardsWeatherDto;
	private JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto;
	private JsaHazardsDroppedDto jsaHazardsDroppedDto;
	private JsaHazardsLiftingDto jsaHazardsLiftingDto;
	private JsaHazardsHeightsDto jsaHazardsHeightsDto;
	private JsaHazardsElectricalDto jsaHazardsElectricalDto;
	private JsaHazardsMovingDto jsaHazardsMovingDto;
	private JsaHazardsManualDto jsaHazardsManualDto;
	private JsaHazardsToolsDto jsaHazardsToolsDto;
	private JsaHazardsFallsDto jsaHazardsFallsDto;
	private JsaHazardsVoltageDto jsaHazardsVoltageDto;
	private JsaHazardsExcavationdDto jsaHazardsExcavationdDto;
	private JsaHazardsMobileDto jsaHazardsMobileDto;
	private List<JsaStepsDto> jsaStepsDtoList;
	private List<JsaStopTriggerDto> jsaStopTriggerDtoList;
	private List<JsaLocationDto> jsaLocationDtoList;
	private List<PtwPeopleDto> ptwPeopleDtoList;
}
