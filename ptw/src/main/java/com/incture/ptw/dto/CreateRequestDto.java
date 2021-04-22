package com.incture.ptw.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateRequestDto {
	private JsaHazardsMobileDto jsaHazardsMobileDto;
	private PtwCwpWorkTypeDto ptwCwpWorkTypeDto;
	private JsaHazardsExcavationdDto jsaHazardsExcavationdDto;
	private JsaHazardsDroppedDto jsaHazardsDroppedDto;
	private JsaHazardsPressurizedDto jsaHazardsPressurizedDto;
	private List<JsaLocationDto> jsaLocationDtoList;
	private List<PtwCloseOutDto> ptwCloseOutDtoList;
	private JsaHazardsVisibilityDto jsaHazardsVisibilityDto;
	private JsaHazardsPersonnelDto jsaHazardsPersonnelDto;
	private JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto;
	private JsaReviewDto jsaReviewDto;
	private JsaHazardsMovingDto jsaHazardsMovingDto;
	private JsaHazardsVoltageDto jsaHazardsVoltageDto;
	private JsaHazardsLiftingDto jsaHazardsLiftingDto;
	private List<JsaStepsDto> jsaStepsDtoList;
	private JsaHazardsManualDto jsaHazardsManualDto;
	private JsaHazardsSpillsDto jsaHazardsSpillsDto;
	private List<JsaStopTriggerDto> jsaStopTriggerDtoList;
	private JsappeDto jsappeDto;
	private JsaHazardsHeightsDto jsaHazardsHeightsDto;
	private JsaHazardsToolsDto jsaHazardsToolsDto;
	private PtwHwpWorkTypeDto ptwHwpWorkTypeDto;
	private JsaheaderDto jsaheaderDto;
	private PtwCseWorkTypeDto ptwCseWorkTypeDto;
	private JsaHazardsIgnitionDto jsaHazardsIgnitionDto;
	private List<PtwTestResultsDto> ptwTestResultsDtoList;
	private JsaHazardscseDto jsaHazardscseDto;
	private JsaHazardsWeatherDto jsaHazardsWeatherDto;
	private JsaRiskAssesmentDto jsaRiskAssesmentDto;
	private List<PtwApprovalDto> ptwApprovalDtoList;
	private List<PtwPeopleDto> ptwPeopleDtoList;
	private JsaHazardsSubstancesDto jsaHazardsSubstancesDto;
	private JsaHazardsElectricalDto jsaHazardsElectricalDto;
	private JsaHazardsFallsDto jsaHazardsFallsDto;
	private List<PtwHeaderDto> ptwHeaderDtoList;
	private List<PtwRequiredDocumentDto> ptwRequiredDocumentDtoList;
	private PtwTestRecordDto ptwTestRecordDto;
	private JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto;
}
