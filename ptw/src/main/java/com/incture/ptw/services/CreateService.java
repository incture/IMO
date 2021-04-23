package com.incture.ptw.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.ptw.dao.JsaHazardsCseDao;
import com.incture.ptw.dao.JsaHazardsDroppedDao;
import com.incture.ptw.dao.JsaHazardsElectricalDao;
import com.incture.ptw.dao.JsaHazardsExcavationdDao;
import com.incture.ptw.dao.JsaHazardsFallsDao;
import com.incture.ptw.dao.JsaHazardsHeightsDao;
import com.incture.ptw.dao.JsaHazardsHighNoiseDao;
import com.incture.ptw.dao.JsaHazardsIgnitionDao;
import com.incture.ptw.dao.JsaHazardsLiftingDao;
import com.incture.ptw.dao.JsaHazardsManualDao;
import com.incture.ptw.dao.JsaHazardsMobileDao;
import com.incture.ptw.dao.JsaHazardsMovingDao;
import com.incture.ptw.dao.JsaHazardsPersonnelDao;
import com.incture.ptw.dao.JsaHazardsPressurizedDao;
import com.incture.ptw.dao.JsaHazardsSimultaneousDao;
import com.incture.ptw.dao.JsaHazardsSpillsDao;
import com.incture.ptw.dao.JsaHazardsSubstancesDao;
import com.incture.ptw.dao.JsaHazardsToolsDao;
import com.incture.ptw.dao.JsaHazardsVisibilityDao;
import com.incture.ptw.dao.JsaHazardsVoltageDao;
import com.incture.ptw.dao.JsaHazardsWeatherDao;
import com.incture.ptw.dao.JsaHeaderDao;
import com.incture.ptw.dao.JsaLocationDao;
import com.incture.ptw.dao.JsaReviewDao;
import com.incture.ptw.dao.JsaRiskAssessmentDao;
import com.incture.ptw.dao.JsaStepsDao;
import com.incture.ptw.dao.JsaStopTriggerDao;
import com.incture.ptw.dao.JsappeDao;
import com.incture.ptw.dao.KeyGeneratorDao;
import com.incture.ptw.dao.PtwCseWorkTypeDao;
import com.incture.ptw.dao.PtwCwpWorkTypeDao;
import com.incture.ptw.dao.PtwHeaderDao;
import com.incture.ptw.dao.PtwHwpWorkTypeDao;
import com.incture.ptw.dao.PtwPeopleDao;
import com.incture.ptw.dao.PtwRequiredDocumentDao;
import com.incture.ptw.dao.PtwTestRecordDao;
import com.incture.ptw.dao.PtwTestResultsDao;
import com.incture.ptw.dto.CreateRequestDto;
import com.incture.ptw.dto.CreateServiceResponseDto;
import com.incture.ptw.dto.JsaLocationDto;
import com.incture.ptw.dto.JsaStepsDto;
import com.incture.ptw.dto.JsaStopTriggerDto;
import com.incture.ptw.dto.PtwHeaderDto;
import com.incture.ptw.dto.PtwPeopleDto;
import com.incture.ptw.dto.PtwRequiredDocumentDto;
import com.incture.ptw.dto.PtwTestResultsDto;
import com.incture.ptw.util.ResponseDto;

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

	@Transactional
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
					if (i.getIsCwp() == 1) {
						ptwHeader = "CWP" + permitNumber;
						isCwp = true;
						ptwPermitNumberList.add(ptwHeader);

					} else if (i.getIsHwp() == 1) {
						ptwHeader = "HWP" + permitNumber;

						isHwp = true;
						ptwPermitNumberList.add(ptwHeader);
					} else if (i.getIsCse() == 1) {
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
			createServiceResponseDto.setSuccess("JSA " + permitNumber + "  created successfully");
			responseDto.setData(createServiceResponseDto);
			responseDto.setMessage("JSA " + permitNumber + "  created successfully");

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
