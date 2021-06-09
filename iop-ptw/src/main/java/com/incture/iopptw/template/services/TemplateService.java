package com.incture.iopptw.template.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.JsaHazardsIgnitionDto;
import com.incture.iopptw.dtos.JsaHazardsPersonnelDto;
import com.incture.iopptw.dtos.JsaHazardsPressurizedDto;
import com.incture.iopptw.dtos.JsaHazardsSimultaneousDto;
import com.incture.iopptw.dtos.JsaHazardsVisibilityDto;
import com.incture.iopptw.dtos.JsaHazardscseDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.dtos.JsappeDto;
import com.incture.iopptw.repositories.KeyGeneratorDao;
import com.incture.iopptw.template.dtos.CreateTemplateDto;
import com.incture.iopptw.template.dtos.TemplateDto;
import com.incture.iopptw.template.repositories.JsaHazardsCseTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsIgnitionTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPersonnelTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsPressurizedTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsSimultaneousTemplateDao;
import com.incture.iopptw.template.repositories.JsaHazardsVisibilityTemplateDao;
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
