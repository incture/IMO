package com.incture.iopptw.template.dtos;

import java.util.List;

import com.incture.iopptw.dtos.JsaHazardsDroppedDto;
import com.incture.iopptw.dtos.JsaHazardsElectricalDto;
import com.incture.iopptw.dtos.JsaHazardsExcavationdDto;
import com.incture.iopptw.dtos.JsaHazardsFallsDto;
import com.incture.iopptw.dtos.JsaHazardsHeightsDto;
import com.incture.iopptw.dtos.JsaHazardsHighNoiseDto;
import com.incture.iopptw.dtos.JsaHazardsIgnitionDto;
import com.incture.iopptw.dtos.JsaHazardsLiftingDto;
import com.incture.iopptw.dtos.JsaHazardsManualDto;
import com.incture.iopptw.dtos.JsaHazardsMobileDto;
import com.incture.iopptw.dtos.JsaHazardsMovingDto;
import com.incture.iopptw.dtos.JsaHazardsPersonnelDto;
import com.incture.iopptw.dtos.JsaHazardsPressurizedDto;
import com.incture.iopptw.dtos.JsaHazardsSimultaneousDto;
import com.incture.iopptw.dtos.JsaHazardsSpillsDto;
import com.incture.iopptw.dtos.JsaHazardsSubstancesDto;
import com.incture.iopptw.dtos.JsaHazardsToolsDto;
import com.incture.iopptw.dtos.JsaHazardsVisibilityDto;
import com.incture.iopptw.dtos.JsaHazardsVoltageDto;
import com.incture.iopptw.dtos.JsaHazardsWeatherDto;
import com.incture.iopptw.dtos.JsaHazardscseDto;
import com.incture.iopptw.dtos.JsaRiskAssesmentDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.dtos.JsappeDto;

import lombok.Data;

@Data
public class CreateTemplateDto {
	private String name;
	private JsaheaderDto jsaheaderDto;
	private JsappeDto jsappeDto;
	private List<JsaStepsDto> jsaStepsDto;
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
	private List<JsaStopTriggerDto> jsaStopTriggerDto;
	private JsaRiskAssesmentDto jsaRiskAssesmentDto;
}
