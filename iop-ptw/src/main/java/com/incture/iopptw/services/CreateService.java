package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.CreateRequestDto;
import com.incture.iopptw.dtos.CreateServiceResponseDto;
import com.incture.iopptw.dtos.JsaLocationDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.dtos.PtwHeaderDto;
import com.incture.iopptw.dtos.PtwPeopleDto;
import com.incture.iopptw.dtos.PtwRequiredDocumentDto;
import com.incture.iopptw.dtos.PtwTestResultsDto;
import com.incture.iopptw.repositories.JsaHazardsCseDao;
import com.incture.iopptw.repositories.JsaHazardsDroppedDao;
import com.incture.iopptw.repositories.JsaHazardsElectricalDao;
import com.incture.iopptw.repositories.JsaHazardsExcavationdDao;
import com.incture.iopptw.repositories.JsaHazardsFallsDao;
import com.incture.iopptw.repositories.JsaHazardsHeightsDao;
import com.incture.iopptw.repositories.JsaHazardsHighNoiseDao;
import com.incture.iopptw.repositories.JsaHazardsIgnitionDao;
import com.incture.iopptw.repositories.JsaHazardsLiftingDao;
import com.incture.iopptw.repositories.JsaHazardsManualDao;
import com.incture.iopptw.repositories.JsaHazardsMobileDao;
import com.incture.iopptw.repositories.JsaHazardsMovingDao;
import com.incture.iopptw.repositories.JsaHazardsPersonnelDao;
import com.incture.iopptw.repositories.JsaHazardsPressurizedDao;
import com.incture.iopptw.repositories.JsaHazardsSimultaneousDao;
import com.incture.iopptw.repositories.JsaHazardsSpillsDao;
import com.incture.iopptw.repositories.JsaHazardsSubstancesDao;
import com.incture.iopptw.repositories.JsaHazardsToolsDao;
import com.incture.iopptw.repositories.JsaHazardsVisibilityDao;
import com.incture.iopptw.repositories.JsaHazardsVoltageDao;
import com.incture.iopptw.repositories.JsaHazardsWeatherDao;
import com.incture.iopptw.repositories.JsaHeaderDao;
import com.incture.iopptw.repositories.JsaLocationDao;
import com.incture.iopptw.repositories.JsaReviewDao;
import com.incture.iopptw.repositories.JsaRiskAssessmentDao;
import com.incture.iopptw.repositories.JsaStepsDao;
import com.incture.iopptw.repositories.JsaStopTriggerDao;
import com.incture.iopptw.repositories.JsappeDao;
import com.incture.iopptw.repositories.KeyGeneratorDao;
import com.incture.iopptw.repositories.PtwCseWorkTypeDao;
import com.incture.iopptw.repositories.PtwCwpWorkTypeDao;
import com.incture.iopptw.repositories.PtwHeaderDao;
import com.incture.iopptw.repositories.PtwHwpWorkTypeDao;
import com.incture.iopptw.repositories.PtwPeopleDao;
import com.incture.iopptw.repositories.PtwRequiredDocumentDao;
import com.incture.iopptw.repositories.PtwTestRecordDao;
import com.incture.iopptw.repositories.PtwTestResultsDao;
import com.incture.iopptw.utils.ResponseDto;

@Service
public class CreateService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JsaHeaderDao jsaHeaderDao;
	@Autowired
	private JsaReviewDao jsaReviewDao;
	@Autowired
	private JsaRiskAssessmentDao jsaRiskAssessmentDao;
	@Autowired
	private JsappeDao jsappeDao;
	@Autowired
	private PtwPeopleDao ptwPeopleDao;
	@Autowired
	private JsaHazardsPressurizedDao jsaHazardsPressurizedDao;
	@Autowired
	private JsaHazardsVisibilityDao jsaHazardsVisibilityDao;
	@Autowired
	private JsaHazardsPersonnelDao jsaHazardsPersonnelDao;
	@Autowired
	private JsaHazardsCseDao jsaHazardsCseDao;
	@Autowired
	private JsaHazardsSimultaneousDao jsaHazardsSimultaneousDao;
	@Autowired
	private JsaHazardsIgnitionDao jsaHazardsIgnitionDao;
	@Autowired
	private JsaHazardsSubstancesDao jsaHazardsSubstancesDao;
	@Autowired
	private JsaHazardsSpillsDao jsaHazardsSpillsDao;
	@Autowired
	private JsaHazardsWeatherDao jsaHazardsWeatherDao;
	@Autowired
	private JsaHazardsHighNoiseDao jsaHazardsHighNoiseDao;
	@Autowired
	private JsaHazardsDroppedDao jsaHazardsDroppedDao;
	@Autowired
	private JsaHazardsLiftingDao jsaHazardsLiftingDao;
	@Autowired
	private JsaHazardsHeightsDao jsaHazardsHeightsDao;
	@Autowired
	private JsaHazardsElectricalDao jsaHazardsElectricalDao;
	@Autowired
	private JsaHazardsMovingDao jsaHazardsMovingDao;
	@Autowired
	private JsaHazardsManualDao jsaHazardsManualDao;
	@Autowired
	private JsaHazardsToolsDao jsaHazardsToolsDao;
	@Autowired
	private JsaHazardsFallsDao jsaHazardsFallsDao;
	@Autowired
	private JsaHazardsVoltageDao jsaHazardsVoltageDao;
	@Autowired
	private JsaHazardsExcavationdDao jsaHazardsExcavationdDao;
	@Autowired
	private JsaHazardsMobileDao jsaHazardsMobileDao;
	@Autowired
	private JsaStepsDao jsaStepsDao;
	@Autowired
	private JsaStopTriggerDao jsaStopTriggerDao;
	@Autowired
	private JsaLocationDao jsaLocationDao;
	@Autowired
	private PtwHeaderDao ptwHeaderDao;
	@Autowired
	private PtwRequiredDocumentDao ptwRequiredDocumentDao;
	@Autowired
	private PtwTestRecordDao ptwTestRecordDao;
	@Autowired
	private PtwTestResultsDao ptwTestResultsDao;
	@Autowired
	private PtwCwpWorkTypeDao ptwCwpWorkTypeDao;
	@Autowired
	private PtwHwpWorkTypeDao ptwHwpWorkTypeDao;
	@Autowired
	private PtwCseWorkTypeDao ptwCseWorkTypeDao;

	@Autowired
	private KeyGeneratorDao keyGeneratorDao;

	public ResponseDto createService(CreateRequestDto createRequestDto) {
		logger.info("CreateService || createService || createDto: " + createRequestDto);
		CreateServiceResponseDto createServiceResponseDto = new CreateServiceResponseDto();

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			String permitNumber = keyGeneratorDao.getPermitNumber();
			boolean isCwp = false;
			boolean isCse = false;
			boolean isHwp = false;
			if (createRequestDto.getJsaheaderDto() != null) {
				jsaHeaderDao.insertJsaHeader(permitNumber, createRequestDto.getJsaheaderDto());
			}
			if (createRequestDto.getJsaReviewDto() != null) {
				jsaReviewDao.insertJsaReview(permitNumber, createRequestDto.getJsaReviewDto());
			}
			if (createRequestDto.getJsaRiskAssesmentDto() != null) {
				jsaRiskAssessmentDao.insertJsaRiskAssessment(permitNumber, createRequestDto.getJsaRiskAssesmentDto());
			}
			if (createRequestDto.getJsappeDto() != null) {
				jsappeDao.insertJsappe(permitNumber, createRequestDto.getJsappeDto());
			}
			if (createRequestDto.getPtwPeopleDtoList() != null || !createRequestDto.getPtwPeopleDtoList().isEmpty()) {
				for (PtwPeopleDto i : createRequestDto.getPtwPeopleDtoList()) {
					ptwPeopleDao.insertPtwPeople(permitNumber, i);
				}

			}
			if (createRequestDto.getJsaHazardsPressurizedDto() != null) {
				jsaHazardsPressurizedDao.insertJsaHazardsPressurized(permitNumber,
						createRequestDto.getJsaHazardsPressurizedDto());
			}
			if (createRequestDto.getJsaHazardsVisibilityDto() != null) {
				jsaHazardsVisibilityDao.insertJsaHazardsVisibility(permitNumber,
						createRequestDto.getJsaHazardsVisibilityDto());
			}
			if (createRequestDto.getJsaHazardsPersonnelDto() != null) {
				jsaHazardsPersonnelDao.insertJsaHazardsPersonnel(permitNumber,
						createRequestDto.getJsaHazardsPersonnelDto());
			}
			if (createRequestDto.getJsaHazardscseDto() != null) {
				jsaHazardsCseDao.insertJsaHazardsCse(permitNumber, createRequestDto.getJsaHazardscseDto());
			}
			if (createRequestDto.getJsaHazardsSimultaneousDto() != null) {
				jsaHazardsSimultaneousDao.insertJsaHazardsSimultaneous(permitNumber,
						createRequestDto.getJsaHazardsSimultaneousDto());
			}
			if (createRequestDto.getJsaHazardsIgnitionDto() != null) {
				jsaHazardsIgnitionDao.insertJsaHazardsIgnition(permitNumber,
						createRequestDto.getJsaHazardsIgnitionDto());
			}
			if (createRequestDto.getJsaHazardsSubstancesDto() != null) {
				jsaHazardsSubstancesDao.insertJsaHazardsSubstances(permitNumber,
						createRequestDto.getJsaHazardsSubstancesDto());
			}
			if (createRequestDto.getJsaHazardsSpillsDto() != null) {
				jsaHazardsSpillsDao.insertJsaHazardsSpills(permitNumber, createRequestDto.getJsaHazardsSpillsDto());
			}
			if (createRequestDto.getJsaHazardsWeatherDto() != null) {
				jsaHazardsWeatherDao.insertJsaHazardsWeather(permitNumber, createRequestDto.getJsaHazardsWeatherDto());
			}
			if (createRequestDto.getJsaHazardsHighNoiseDto() != null) {
				jsaHazardsHighNoiseDao.insertJsaHazardsHighNoise(permitNumber,
						createRequestDto.getJsaHazardsHighNoiseDto());
			}
			if (createRequestDto.getJsaHazardsDroppedDto() != null) {
				jsaHazardsDroppedDao.insertJsaHazardsDropped(permitNumber, createRequestDto.getJsaHazardsDroppedDto());
			}
			if (createRequestDto.getJsaHazardsLiftingDto() != null) {
				jsaHazardsLiftingDao.insertJsaHazardsLifting(permitNumber, createRequestDto.getJsaHazardsLiftingDto());
			}
			if (createRequestDto.getJsaHazardsHeightsDto() != null) {
				jsaHazardsHeightsDao.insertJsaHazardsHeights(permitNumber, createRequestDto.getJsaHazardsHeightsDto());
			}
			if (createRequestDto.getJsaHazardsElectricalDto() != null) {
				jsaHazardsElectricalDao.insertJsaHazardsElectrical(permitNumber,
						createRequestDto.getJsaHazardsElectricalDto());
			}
			if (createRequestDto.getJsaHazardsMovingDto() != null) {
				jsaHazardsMovingDao.insertJsaHazardsMoving(permitNumber, createRequestDto.getJsaHazardsMovingDto());
			}
			if (createRequestDto.getJsaHazardsManualDto() != null) {
				jsaHazardsManualDao.insertJsaHazardsManual(permitNumber, createRequestDto.getJsaHazardsManualDto());
			}
			if (createRequestDto.getJsaHazardsToolsDto() != null) {
				jsaHazardsToolsDao.insertJsaHazardsTools(permitNumber, createRequestDto.getJsaHazardsToolsDto());
			}
			if (createRequestDto.getJsaHazardsFallsDto() != null) {
				jsaHazardsFallsDao.insertJsaHazardsFalls(permitNumber, createRequestDto.getJsaHazardsFallsDto());
			}
			if (createRequestDto.getJsaHazardsVoltageDto() != null) {
				jsaHazardsVoltageDao.insertJsaHazardsVoltage(permitNumber, createRequestDto.getJsaHazardsVoltageDto());
			}
			if (createRequestDto.getJsaHazardsExcavationdDto() != null) {
				jsaHazardsExcavationdDao.insertJsaHazardsExcavation(permitNumber,
						createRequestDto.getJsaHazardsExcavationdDto());
			}
			if (createRequestDto.getJsaHazardsMobileDto() != null) {
				jsaHazardsMobileDao.insertJsaHazardsMobile(permitNumber, createRequestDto.getJsaHazardsMobileDto());
			}
			if (createRequestDto.getJsaStepsDtoList() != null || !createRequestDto.getJsaStepsDtoList().isEmpty()) {
				for (JsaStepsDto i : createRequestDto.getJsaStepsDtoList()) {
					jsaStepsDao.insertJsaSteps(permitNumber, i);
				}

			}
			if (createRequestDto.getJsaStopTriggerDtoList() != null
					|| !createRequestDto.getJsaStopTriggerDtoList().isEmpty()) {
				for (JsaStopTriggerDto i : createRequestDto.getJsaStopTriggerDtoList()) {
					jsaStopTriggerDao.insertJsaStopTrigger(permitNumber, i);
				}

			}
			if (createRequestDto.getJsaLocationDtoList() != null
					|| !createRequestDto.getJsaLocationDtoList().isEmpty()) {
				for (JsaLocationDto i : createRequestDto.getJsaLocationDtoList()) {
					jsaLocationDao.insertJsaLocation(permitNumber, i);
				}

			}
			List<String> ptwPermitNumberList = new ArrayList<>();
			if (createRequestDto.getPtwHeaderDtoList() != null || !createRequestDto.getPtwHeaderDtoList().isEmpty()) {
				for (PtwHeaderDto i : createRequestDto.getPtwHeaderDtoList()) {
					String ptwHeader = "";
					if (i.getIsCWP() == 1) {
						ptwHeader = "CWP" + permitNumber;
						isCwp = true;
						ptwPermitNumberList.add(ptwHeader);

					} else if (i.getIsHWP() == 1) {
						ptwHeader = "HWP" + permitNumber;

						isHwp = true;
						ptwPermitNumberList.add(ptwHeader);
					} else if (i.getIsCSE() == 1) {
						ptwHeader = "CSE" + permitNumber;
						isCse = true;
						ptwPermitNumberList.add(ptwHeader);
					}
					createServiceResponseDto.setPtwPermitNumber(ptwPermitNumberList);
					ptwHeaderDao.insertPtwHeader(permitNumber, ptwHeader, i);
				}

			}
			if (createRequestDto.getPtwRequiredDocumentDtoList() != null
					|| !createRequestDto.getPtwRequiredDocumentDtoList().isEmpty()) {
				for (PtwRequiredDocumentDto i : createRequestDto.getPtwRequiredDocumentDtoList()) {
					ptwRequiredDocumentDao.insertPtwRequiredDocument(permitNumber, i);
				}

			}
			if (createRequestDto.getPtwTestRecordDto() != null) {
				ptwTestRecordDao.insertPtwTestRecord(permitNumber, createRequestDto.getPtwTestRecordDto());
			}
			if (createRequestDto.getPtwTestResultsDtoList() != null
					|| !createRequestDto.getPtwTestResultsDtoList().isEmpty()) {
				for (PtwTestResultsDto i : createRequestDto.getPtwTestResultsDtoList()) {
					ptwTestResultsDao.insertPtwTestResults(permitNumber, i);
				}

			}
			if (createRequestDto.getPtwCwpWorkTypeDto() != null && isCwp == true) {
				ptwCwpWorkTypeDao.insertPtwCwpWorkType(permitNumber, createRequestDto.getPtwCwpWorkTypeDto());
			}
			if (createRequestDto.getPtwHwpWorkTypeDto() != null && isHwp == true) {
				ptwHwpWorkTypeDao.insertPtwHwpWorkType(permitNumber, createRequestDto.getPtwHwpWorkTypeDto());
			}
			if (createRequestDto.getPtwCseWorkTypeDto() != null && isCse == true) {
				ptwCseWorkTypeDao.insertPtwCseWorkType(permitNumber, createRequestDto.getPtwCseWorkTypeDto());
			}
			jsaHeaderDao.updateJsaHeader(permitNumber, createRequestDto.getJsaheaderDto());
			jsaReviewDao.updateJsaReview(permitNumber, createRequestDto.getJsaReviewDto());
			createServiceResponseDto.setJsaPermitNumber("JSA" + permitNumber);
			createServiceResponseDto.setPermitNumber(permitNumber);
			createServiceResponseDto.setSuccess("JSA " + permitNumber + " created successfully");
			responseDto.setData(createServiceResponseDto);
			responseDto.setMessage("JSA " + permitNumber + " created successfully");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CreateService || createService: " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage("Create JSA Service Error :" + e.getMessage());

		}

		logger.info("CreateService || createService: " + responseDto);

		return responseDto;

	}

}
