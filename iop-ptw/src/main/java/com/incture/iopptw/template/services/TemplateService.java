package com.incture.iopptw.template.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.JsaHazardsDroppedDto;
import com.incture.iopptw.dtos.JsaHazardsElectricalDto;
import com.incture.iopptw.dtos.JsaHazardsHeightsDto;
import com.incture.iopptw.dtos.JsaHazardsHighNoiseDto;
import com.incture.iopptw.dtos.JsaHazardsIgnitionDto;
import com.incture.iopptw.dtos.JsaHazardsLiftingDto;
import com.incture.iopptw.dtos.JsaHazardsManualDto;
import com.incture.iopptw.dtos.JsaHazardsMovingDto;
import com.incture.iopptw.dtos.JsaHazardsPersonnelDto;
import com.incture.iopptw.dtos.JsaHazardsPressurizedDto;
import com.incture.iopptw.dtos.JsaHazardsSimultaneousDto;
import com.incture.iopptw.dtos.JsaHazardsSpillsDto;
import com.incture.iopptw.dtos.JsaHazardsSubstancesDto;
import com.incture.iopptw.dtos.JsaHazardsToolsDto;
import com.incture.iopptw.dtos.JsaHazardsVisibilityDto;
import com.incture.iopptw.dtos.JsaHazardsWeatherDto;
import com.incture.iopptw.dtos.JsaHazardscseDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.dtos.JsappeDto;
import com.incture.iopptw.repositories.KeyGeneratorDao;
import com.incture.iopptw.template.dtos.CreateTemplateDto;
import com.incture.iopptw.template.dtos.TemplateDto;
import com.incture.iopptw.template.repositories.JsaHazardsCseTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsDroppedTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsElectricalTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsHeightsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsHighNoiseTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsIgnitionTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsLiftingTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsManualTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsMovingTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPersonnelTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPressurizedTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSimultaneousTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSpillsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSubstancesTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsToolsTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsVisibilityTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsWeatherTemplateDao;
import com.incture.iopptw.template.repositories.JsaHeaderTemplateDao;
import com.incture.iopptw.template.repositories.JsaStepsTemplateDao;
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
	public ResponseDto createTemplateService(TemplateDto templateDto) {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(true);
		responseDto.setStatusCode(200);
		try {
			templateDto.setId(Integer.parseInt(keyGeneratorDao.getTEMPLATE()));
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
			JsaheaderDto jsaheaderDto = jsaHeaderTemplateDao.getJsaHeader(tmpId);
			createTemplateDto.setJsaheaderDto(jsaheaderDto);

			JsappeDto jsappeDto = jsappeTemplateDao.getJsappe(tmpId);
			createTemplateDto.setJsappeDto(jsappeDto);

			JsaStepsDto jsaStepsDto = jsaStepsTemplateDao.getJsaStepsDto(tmpId);
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
			
			responseDto.setData(createTemplateDto);
			responseDto.setMessage("Success");
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

}
