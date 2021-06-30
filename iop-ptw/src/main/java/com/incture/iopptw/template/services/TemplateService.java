package com.incture.iopptw.template.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.incture.iopptw.repositories.KeyGeneratorDao;
import com.incture.iopptw.template.dtos.CreateTemplateDto;
import com.incture.iopptw.template.dtos.TemplateDto;
import com.incture.iopptw.template.repositories.JsaHazardsCseTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsDroppedTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsElectricalTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsExcavationTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsFallsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsHeightsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsHighNoiseTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsIgnitionTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsLiftingTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsManualTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsMobileTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsMovingTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPersonnelTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPressurizedTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSimultaneousTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSpillsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSubstancesTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsToolsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsVisibilityTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsVoltageTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsWeatherTemplateDao;
import com.incture.iopptw.template.repositories.JsaHeaderTemplateDao;
import com.incture.iopptw.template.repositories.JsaRiskAssesmentTemplateDao;
import com.incture.iopptw.template.repositories.JsaStepsTemplateDao;
import com.incture.iopptw.template.repositories.JsaStopTriggerTemplateDao;
import com.incture.iopptw.template.repositories.JsappeTemplateDao;
import com.incture.iopptw.template.repositories.TemplateDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
@Transactional
public class TemplateService {
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private KeyGeneratorDao keyGeneratorDao;
	@Autowired
	private JsaHeaderTemplateDao jsaHeaderTemplateDao;
	@Autowired
	private JsappeTemplateDao jsappeTemplateDao;
	@Autowired
	private JsaStepsTemplateDao jsaStepsTemplateDao;
	@Autowired
	private JsaHazardsPressurizedTemplateDao jsaHazardsPressurizedTemplateDao;
	@Autowired
	private JsaHazardsVisibilityTemplateDao jsaHazardsVisibilityTemplateDao;
	@Autowired
	private JsaHazardsPersonnelTemplateDao jsaHazardsPersonnelTemplateDao;
	@Autowired
	private JsaHazardsCseTemplateDao jsaHazardsCseTemplateDao;
	@Autowired
	private JsaHazardsSimultaneousTemplateDao jsaHazardsSimultaneousTemplateDao;
	@Autowired
	private JsaHazardsIgnitionTemplateDao jsaHazardsIgnitionTemplateDao;
	@Autowired
	private JsaHazardsSubstancesTemplateDao jsaHazardsSubstancesTemplateDao;
	@Autowired
	private JsaHazardsSpillsTemplateDao jsaHazardsSpillsTemplateDao;
	@Autowired
	private JsaHazardsWeatherTemplateDao jsaHazardsWeatherTemplateDao;
	@Autowired
	private JsaHazardsHighNoiseTemplateDao jsaHazardsHighNoiseTemplateDao;
	@Autowired
	private JsaHazardsDroppedTemplateDao jsaHazardsDroppedTemplateDao;
	@Autowired
	private JsaHazardsLiftingTemplateDao jsaHazardsLiftingTemplateDao;
	@Autowired
	private JsaHazardsHeightsTemplateDao jsaHazardsHeightsTemplateDao;
	@Autowired
	private JsaHazardsElectricalTemplateDao jsaHazardsElectricalTemplateDao;
	@Autowired
	private JsaHazardsMovingTemplateDao jsaHazardsMovingTemplateDao;
	@Autowired
	private JsaHazardsManualTemplateDao jsaHazardsManualTemplateDao;
	@Autowired
	private JsaHazardsToolsTemplateDao jsaHazardsToolsTemplateDao;
	@Autowired
	private JsaHazardsFallsTemplateDao jsaHazardsFallsTemplateDao;
	@Autowired
	private JsaHazardsVoltageTemplateDao jsaHazardsVoltageTemplateDao;
	@Autowired
	private JsaHazardsExcavationTemplateDao jsaHazardsExcavationTemplateDao;
	@Autowired
	private JsaHazardsMobileTemplateDao jsaHazardsMobileTemplateDao;
	@Autowired
	private JsaStopTriggerTemplateDao jsaStopTriggerTemplateDao;
	@Autowired
	private JsaRiskAssesmentTemplateDao jsaRiskAssesmentTemplateDao;
	
	public ResponseDto createTemplateService(TemplateDto templateDto) {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(true);
		responseDto.setStatusCode(200);
		try {
			templateDto.setId(templateDto.getId());
			templateDao.createTemplate(templateDto);
			responseDto.setMessage("Template Created Successfully");
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

	public ResponseDto getAllTemplateList() {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(true);
		responseDto.setStatusCode(200);
		try {

			responseDto.setData(templateDao.getAllTemplateList());
			responseDto.setMessage("Success");
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

	public ResponseDto getTemplateById(int tmpId) {
		ResponseDto responseDto = new ResponseDto();
		CreateTemplateDto createTemplateDto = new CreateTemplateDto();
		responseDto.setStatus(true);
		responseDto.setStatusCode(200);
		try {
			String name = templateDao.getTemplateNameById(tmpId);
			createTemplateDto.setName(name);
			
			JsaheaderDto jsaheaderDto = jsaHeaderTemplateDao.getJsaHeader(tmpId);
			createTemplateDto.setJsaheaderDto(jsaheaderDto);

			JsappeDto jsappeDto = jsappeTemplateDao.getJsappe(tmpId);
			createTemplateDto.setJsappeDto(jsappeDto);

			List<JsaStepsDto> jsaStepsDto = jsaStepsTemplateDao.getJsaStepsDto(tmpId);
			createTemplateDto.setJsaStepsDto(jsaStepsDto);

			JsaHazardsPressurizedDto jsaHazardsPressurizedDto = jsaHazardsPressurizedTemplateDao
					.getjsaHazardsPress(tmpId);
			createTemplateDto.setJsaHazardsPressurizedDto(jsaHazardsPressurizedDto);

			JsaHazardsVisibilityDto jsaHazardsVisibilityDto = jsaHazardsVisibilityTemplateDao
					.getJsaHazardsVisible(tmpId);
			createTemplateDto.setJsaHazardsVisibilityDto(jsaHazardsVisibilityDto);

			JsaHazardsPersonnelDto jsaHazardsPersonnelDto = jsaHazardsPersonnelTemplateDao.getJsaPersonnel(tmpId);
			createTemplateDto.setJsaHazardsPersonnelDto(jsaHazardsPersonnelDto);

			JsaHazardscseDto jsaHazardscseDto = jsaHazardsCseTemplateDao.getJsaHazardsCse(tmpId);
			createTemplateDto.setJsaHazardscseDto(jsaHazardscseDto);

			JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = jsaHazardsSimultaneousTemplateDao
					.getJsaHazardsSimultan(tmpId);
			createTemplateDto.setJsaHazardsSimultaneousDto(jsaHazardsSimultaneousDto);

			JsaHazardsIgnitionDto jsaHazardsIgnitionDto = jsaHazardsIgnitionTemplateDao.getJsaHazardsIgnition(tmpId);
			createTemplateDto.setJsaHazardsIgnitionDto(jsaHazardsIgnitionDto);

			JsaHazardsSubstancesDto jsaHazardsSubstancesDto = jsaHazardsSubstancesTemplateDao
					.getJsaHazardsSubstances(tmpId);
			createTemplateDto.setJsaHazardsSubstancesDto(jsaHazardsSubstancesDto);

			JsaHazardsSpillsDto jsaHazardsSpillsDto = jsaHazardsSpillsTemplateDao.getJsaHazardsSpillsDto(tmpId);
			createTemplateDto.setJsaHazardsSpillsDto(jsaHazardsSpillsDto);

			JsaHazardsWeatherDto jsaHazardsWeatherDto = jsaHazardsWeatherTemplateDao
					.getJsaHazardsWeatherTemplate(tmpId);
			createTemplateDto.setJsaHazardsWeatherDto(jsaHazardsWeatherDto);

			JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto = jsaHazardsHighNoiseTemplateDao
					.getJsaHazardsHighNoiseDto(tmpId);
			createTemplateDto.setJsaHazardsHighNoiseDto(jsaHazardsHighNoiseDto);

			JsaHazardsDroppedDto jsaHazardsDroppedDto = jsaHazardsDroppedTemplateDao.getJsaHazardsDroppedDto(tmpId);
			createTemplateDto.setJsaHazardsDroppedDto(jsaHazardsDroppedDto);

			JsaHazardsLiftingDto jsaHazardsLiftingDto = jsaHazardsLiftingTemplateDao.getJsaHazardsLiftingDto(tmpId);
			createTemplateDto.setJsaHazardsLiftingDto(jsaHazardsLiftingDto);

			JsaHazardsHeightsDto jsaHazardsHeightsDto = jsaHazardsHeightsTemplateDao.getJsaHazardsHeightsDto(tmpId);
			createTemplateDto.setJsaHazardsHeightsDto(jsaHazardsHeightsDto);

			JsaHazardsElectricalDto jsaHazardsElectricalDto = jsaHazardsElectricalTemplateDao
					.getJsaHazardsElectricalDto(tmpId);
			createTemplateDto.setJsaHazardsElectricalDto(jsaHazardsElectricalDto);

			JsaHazardsMovingDto jsaHazardsMovingDto = jsaHazardsMovingTemplateDao.getJsaHazardsMovingDto(tmpId);
			createTemplateDto.setJsaHazardsMovingDto(jsaHazardsMovingDto);

			JsaHazardsManualDto jsaHazardsManualDto = jsaHazardsManualTemplateDao.getJsaHazardsManualDto(tmpId);
			createTemplateDto.setJsaHazardsManualDto(jsaHazardsManualDto);

			JsaHazardsToolsDto jsaHazardsToolsDto = jsaHazardsToolsTemplateDao.getJsaHazardsToolsDto(tmpId);
			createTemplateDto.setJsaHazardsToolsDto(jsaHazardsToolsDto);

			JsaHazardsFallsDto jsaHazardsFallsDto = jsaHazardsFallsTemplateDao.getJsaHazardsFallsDto(tmpId);
			createTemplateDto.setJsaHazardsFallsDto(jsaHazardsFallsDto);

			JsaHazardsVoltageDto jsaHazardsVoltageDto = jsaHazardsVoltageTemplateDao.getJsaHazardsVoltageDto(tmpId);
			createTemplateDto.setJsaHazardsVoltageDto(jsaHazardsVoltageDto);

			JsaHazardsExcavationdDto jsaHazardsExcavationdDto = jsaHazardsExcavationTemplateDao
					.getJsaHazardsExcavationdDto(tmpId);
			createTemplateDto.setJsaHazardsExcavationdDto(jsaHazardsExcavationdDto);

			JsaHazardsMobileDto jsaHazardsMobileDto = jsaHazardsMobileTemplateDao.getJsaHazardsMobileDto(tmpId);
			createTemplateDto.setJsaHazardsMobileDto(jsaHazardsMobileDto);

			List<JsaStopTriggerDto> jsaStopTriggerDto = jsaStopTriggerTemplateDao.getJsaStopTriggerDto(tmpId);
			createTemplateDto.setJsaStopTriggerDto(jsaStopTriggerDto);
			
			JsaRiskAssesmentDto jsaRiskAssesmentDto = jsaRiskAssesmentTemplateDao.getJsaRiskAss(tmpId);
			createTemplateDto.setJsaRiskAssesmentDto(jsaRiskAssesmentDto);
			
			responseDto.setData(createTemplateDto);
			responseDto.setMessage("Success");
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

	public ResponseDto saveTemplate(TemplateDto templateDto, CreateTemplateDto createTemplateDto) {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(true);
		responseDto.setStatusCode(200);
		try {
			String id = keyGeneratorDao.getTEMPLATE();
			templateDto.setId(id);
			createTemplateService(templateDto);
			if (createTemplateDto.getJsaheaderDto() != null)
				jsaHeaderTemplateDao.insertJsaHeaderTemplate(id, createTemplateDto.getJsaheaderDto());
			if (createTemplateDto.getJsappeDto() != null)
				jsappeTemplateDao.insertJsappeTemplate(id, createTemplateDto.getJsappeDto());
			if (createTemplateDto.getJsaStepsDto() != null){
				for(JsaStepsDto b : createTemplateDto.getJsaStepsDto())
					jsaStepsTemplateDao.insertJsaStepsTemplate(id, b);
			}
			if (createTemplateDto.getJsaHazardsPressurizedDto() != null)
				jsaHazardsPressurizedTemplateDao.insertJsaHazardsPressurizedTemplate(id,
						createTemplateDto.getJsaHazardsPressurizedDto());
			if (createTemplateDto.getJsaHazardsVisibilityDto() != null)
				jsaHazardsVisibilityTemplateDao.insertJsaHazardsVisibilityTemplate(id,
						createTemplateDto.getJsaHazardsVisibilityDto());
			if (createTemplateDto.getJsaHazardsPersonnelDto() != null)
				jsaHazardsPersonnelTemplateDao.insertJsaHazardsPersonnelTemplate(id,
						createTemplateDto.getJsaHazardsPersonnelDto());
			if (createTemplateDto.getJsaHazardscseDto() != null)
				jsaHazardsCseTemplateDao.insertJsaHazardsCseTemplate(id, createTemplateDto.getJsaHazardscseDto());
			if (createTemplateDto.getJsaHazardsSimultaneousDto() != null)
				jsaHazardsSimultaneousTemplateDao.insertJsaHazardsSimultaneousTemplate(id,
						createTemplateDto.getJsaHazardsSimultaneousDto());
			if (createTemplateDto.getJsaHazardsIgnitionDto() != null)
				jsaHazardsIgnitionTemplateDao.insertJsaHazardsIgnitionTemplate(id,
						createTemplateDto.getJsaHazardsIgnitionDto());
			if (createTemplateDto.getJsaHazardsSubstancesDto() != null)
				jsaHazardsSubstancesTemplateDao.insertJsaHazardsSubstancesTemplate(id,
						createTemplateDto.getJsaHazardsSubstancesDto());
			if (createTemplateDto.getJsaHazardsSpillsDto() != null)
				jsaHazardsSpillsTemplateDao.insertJsaHazardsSpillsTemplate(id,
						createTemplateDto.getJsaHazardsSpillsDto());
			if (createTemplateDto.getJsaHazardsWeatherDto() != null)
				jsaHazardsWeatherTemplateDao.insertJsaHazardsWeatherTemplate(id,
						createTemplateDto.getJsaHazardsWeatherDto());
			if (createTemplateDto.getJsaHazardsHighNoiseDto() != null)
				jsaHazardsHighNoiseTemplateDao.insertJsaHazardsHighNoiseTemplate(id,
						createTemplateDto.getJsaHazardsHighNoiseDto());
			if (createTemplateDto.getJsaHazardsDroppedDto() != null)
				jsaHazardsDroppedTemplateDao.insertJsaHazardsDroppedTemplate(id,
						createTemplateDto.getJsaHazardsDroppedDto());
			if (createTemplateDto.getJsaHazardsLiftingDto() != null)
				jsaHazardsLiftingTemplateDao.insertJsaHazardsLiftingTemplate(id,
						createTemplateDto.getJsaHazardsLiftingDto());
			if (createTemplateDto.getJsaHazardsHeightsDto() != null)
				jsaHazardsHeightsTemplateDao.insertJsaHazardsHeights(id, createTemplateDto.getJsaHazardsHeightsDto());
			if (createTemplateDto.getJsaHazardsElectricalDto() != null)
				jsaHazardsElectricalTemplateDao.insertJsaHazardsElectrical(id,
						createTemplateDto.getJsaHazardsElectricalDto());
			if (createTemplateDto.getJsaHazardsMovingDto() != null)
				jsaHazardsMovingTemplateDao.insertJsaHazardsMoving(id, createTemplateDto.getJsaHazardsMovingDto());
			if (createTemplateDto.getJsaHazardsManualDto() != null)
				jsaHazardsManualTemplateDao.insertJsaHazardsManual(id, createTemplateDto.getJsaHazardsManualDto());
			if (createTemplateDto.getJsaHazardsToolsDto() != null)
				jsaHazardsToolsTemplateDao.insertJsaHazardsTools(id, createTemplateDto.getJsaHazardsToolsDto());
			if (createTemplateDto.getJsaHazardsFallsDto() != null)
				jsaHazardsFallsTemplateDao.insertJsaHazardsFalls(id, createTemplateDto.getJsaHazardsFallsDto());
			if (createTemplateDto.getJsaHazardsVoltageDto() != null)
				jsaHazardsVoltageTemplateDao.insertJsaHazardsVoltage(id, createTemplateDto.getJsaHazardsVoltageDto());
			if (createTemplateDto.getJsaHazardsExcavationdDto() != null)
				jsaHazardsExcavationTemplateDao.insertJsaHazardsExcavation(id,
						createTemplateDto.getJsaHazardsExcavationdDto());
			if (createTemplateDto.getJsaHazardsMobileDto() != null)
				jsaHazardsMobileTemplateDao.insertJsaHazardsMobile(id, createTemplateDto.getJsaHazardsMobileDto());
			if (createTemplateDto.getJsaStopTriggerDto() != null){
				for(JsaStopTriggerDto a :createTemplateDto.getJsaStopTriggerDto())
					jsaStopTriggerTemplateDao.insertJsaStopTrigger(id, a);
			}
			//new dto added
			if(createTemplateDto.getJsaRiskAssesmentDto() != null)
				jsaRiskAssesmentTemplateDao.insertJsaRiskAssesment(id, createTemplateDto.getJsaRiskAssesmentDto());
			responseDto.setMessage("Template Created Successfully");
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

}
