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
import com.incture.ptw.dto.JsaLocationDto;
import com.incture.ptw.dto.JsaStepsDto;
import com.incture.ptw.dto.JsaStopTriggerDto;
import com.incture.ptw.dto.PtwHeaderDto;
import com.incture.ptw.dto.PtwPeopleDto;
import com.incture.ptw.dto.PtwRequiredDocumentDto;
import com.incture.ptw.dto.PtwTestResultsDto;
import com.incture.ptw.util.ResponseDto;

@Service
@Transactional
public class JsaUpdateService {
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

	public ResponseDto updateJsaService(CreateRequestDto createRequestDto) {
		logger.info("JsaUpdateService || updateJsaService || createDto: " + createRequestDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			Integer permitNumber = createRequestDto.getJsaheaderDto().getPermitNumber();
			if (createRequestDto.getJsaheaderDto() != null) {
				jsaHeaderDao.updateJsaHeaderByPermitNumber(createRequestDto.getJsaheaderDto());
			}
			if (createRequestDto.getJsaReviewDto() != null) {
				jsaReviewDao.updateJsaReview(createRequestDto.getJsaReviewDto());
			}
			if (createRequestDto.getJsaRiskAssesmentDto() != null) {
				jsaRiskAssessmentDao.updateJsaRiskAssessment(createRequestDto.getJsaRiskAssesmentDto());
			}
			if (createRequestDto.getJsappeDto() != null) {
				jsappeDao.updateJsappe(createRequestDto.getJsappeDto());
			}
			if (createRequestDto.getJsaHazardsPressurizedDto() != null) {
				jsaHazardsPressurizedDao.updateJsaHazardsPressurized(createRequestDto.getJsaHazardsPressurizedDto());
			}

			if (createRequestDto.getJsaHazardsVisibilityDto() != null) {
				jsaHazardsVisibilityDao.updateJsaHazardsVisibility(createRequestDto.getJsaHazardsVisibilityDto());
			}
			if (createRequestDto.getJsaHazardsPersonnelDto() != null) {
				jsaHazardsPersonnelDao.updateJsaHazardsPersonnel(createRequestDto.getJsaHazardsPersonnelDto());
			}
			if (createRequestDto.getJsaHazardscseDto() != null) {
				jsaHazardsCseDao.updateJsaHazardsCse(createRequestDto.getJsaHazardscseDto());
			}
			if (createRequestDto.getJsaHazardsSimultaneousDto() != null) {
				jsaHazardsSimultaneousDao.updateJsaHazardsSimultaneous(createRequestDto.getJsaHazardsSimultaneousDto());
			}
			if (createRequestDto.getJsaHazardsIgnitionDto() != null) {
				jsaHazardsIgnitionDao.updateJsaHazardsIgnition(createRequestDto.getJsaHazardsIgnitionDto());
			}
			if (createRequestDto.getJsaHazardsSubstancesDto() != null) {
				jsaHazardsSubstancesDao.updateJsaHazardsSubstances(createRequestDto.getJsaHazardsSubstancesDto());
			}
			if (createRequestDto.getJsaHazardsSpillsDto() != null) {
				jsaHazardsSpillsDao.updateJsaHazardsSpills(createRequestDto.getJsaHazardsSpillsDto());
			}
			if (createRequestDto.getJsaHazardsWeatherDto() != null) {
				jsaHazardsWeatherDao.updateJsaHazardsWeather(createRequestDto.getJsaHazardsWeatherDto());
			}
			if (createRequestDto.getJsaHazardsHighNoiseDto() != null) {
				jsaHazardsHighNoiseDao.updateJsaHazardsHighNoise(createRequestDto.getJsaHazardsHighNoiseDto());
			}
			if (createRequestDto.getJsaHazardsDroppedDto() != null) {
				jsaHazardsDroppedDao.updateJsaHazardsDropped(createRequestDto.getJsaHazardsDroppedDto());
			}
			if (createRequestDto.getJsaHazardsLiftingDto() != null) {
				jsaHazardsLiftingDao.updateJsaHazardsLifting(createRequestDto.getJsaHazardsLiftingDto());
			}
//			if (createRequestDto.getJsaHazardsHeightsDto() != null) {
//				jsaHazardsHeightsDao.updateJsaHazardsHeights(createRequestDto.getJsaHazardsHeightsDto());
//			}
//			if (createRequestDto.getJsaHazardsElectricalDto() != null) {
//				jsaHazardsElectricalDao.updateJsaHazardsElectrical(createRequestDto.getJsaHazardsElectricalDto());
//			}
//			if (createRequestDto.getJsaHazardsMovingDto() != null) {
//				jsaHazardsMovingDao.updateJsaHazardsMoving(createRequestDto.getJsaHazardsMovingDto());
//			}
//			if (createRequestDto.getJsaHazardsManualDto() != null) {
//				jsaHazardsManualDao.updateJsaHazardsManual(createRequestDto.getJsaHazardsManualDto());
//			}
//			if (createRequestDto.getJsaHazardsToolsDto() != null) {
//				jsaHazardsToolsDao.updateJsaHazardsTools(createRequestDto.getJsaHazardsToolsDto());
//			}
//			if (createRequestDto.getJsaHazardsFallsDto() != null) {
//				jsaHazardsFallsDao.updateJsaHazardsFalls(createRequestDto.getJsaHazardsFallsDto());
//			}
//			if (createRequestDto.getJsaHazardsVoltageDto() != null) {
//				jsaHazardsVoltageDao.updateJsaHazardsVoltage(createRequestDto.getJsaHazardsVoltageDto());
//			}
//			if (createRequestDto.getJsaHazardsExcavationdDto() != null) {
//				jsaHazardsExcavationdDao.updateJsaHazardsExcavation(createRequestDto.getJsaHazardsExcavationdDto());
//			}
			if (createRequestDto.getJsaHazardsMobileDto() != null) {
				jsaHazardsMobileDao.updateJsaHazardsMobile(createRequestDto.getJsaHazardsMobileDto());
			}
//			// delete jsasteps
//			if (createRequestDto.getJsaStepsDtoList() != null || !createRequestDto.getJsaStepsDtoList().isEmpty()) {
//				for (JsaStepsDto i : createRequestDto.getJsaStepsDtoList()) {
//					jsaStepsDao.insertJsaSteps(permitNumber, i);
//				}
//
//			}
//			// delete jsastoptrigger
//			if (createRequestDto.getJsaStopTriggerDtoList() != null
//					|| !createRequestDto.getJsaStopTriggerDtoList().isEmpty()) {
//				for (JsaStopTriggerDto i : createRequestDto.getJsaStopTriggerDtoList()) {
//					jsaStopTriggerDao.insertJsaStopTrigger(permitNumber, i);
//				}
//
//			}
//			// delete jsa_location
//			if (createRequestDto.getJsaLocationDtoList() != null
//					|| !createRequestDto.getJsaLocationDtoList().isEmpty()) {
//				for (JsaLocationDto i : createRequestDto.getJsaLocationDtoList()) {
//					jsaLocationDao.insertJsaLocation(permitNumber, i);
//				}
//
//			}
//			// delete ptwpeople
//			if (createRequestDto.getPtwPeopleDtoList() != null || !createRequestDto.getPtwPeopleDtoList().isEmpty()) {
//				for (PtwPeopleDto i : createRequestDto.getPtwPeopleDtoList()) {
//					ptwPeopleDao.insertPtwPeople(permitNumber, i);
//				}
//
//			}
//			List<String> ptwPermitNumberList = new ArrayList<>();
//			if (createRequestDto.getPtwHeaderDtoList() != null || !createRequestDto.getPtwHeaderDtoList().isEmpty()) {
//				for (PtwHeaderDto i : createRequestDto.getPtwHeaderDtoList()) {
//					String ptwHeader = "";
//					if (i.getIsCWP() == 1) {
//						ptwHeader = "CWP" + permitNumber;
//						isCwp = true;
//						ptwPermitNumberList.add(ptwHeader);
//
//					} else if (i.getIsHWP() == 1) {
//						ptwHeader = "HWP" + permitNumber;
//
//						isHwp = true;
//						ptwPermitNumberList.add(ptwHeader);
//					} else if (i.getIsCSE() == 1) {
//						ptwHeader = "CSE" + permitNumber;
//						isCse = true;
//						ptwPermitNumberList.add(ptwHeader);
//					}
//					createServiceResponseDto.setPtwPermitNumber(ptwPermitNumberList);
//					ptwHeaderDao.insertPtwHeader(permitNumber, ptwHeader, i);
//				}
//
//			}
//			if (createRequestDto.getPtwRequiredDocumentDtoList() != null
//					|| !createRequestDto.getPtwRequiredDocumentDtoList().isEmpty()) {
//				for (PtwRequiredDocumentDto i : createRequestDto.getPtwRequiredDocumentDtoList()) {
//					ptwRequiredDocumentDao.insertPtwRequiredDocument(permitNumber, i);
//				}
//
//			}
//			// intert into ptwapproval
//			// delete ptwtestrecord
//			if (createRequestDto.getPtwTestRecordDto() != null) {
//				ptwTestRecordDao.insertPtwTestRecord(permitNumber, createRequestDto.getPtwTestRecordDto());
//			}
//			// delete ptwtestresult
//			if (createRequestDto.getPtwTestResultsDtoList() != null
//					|| !createRequestDto.getPtwTestResultsDtoList().isEmpty()) {
//				for (PtwTestResultsDto i : createRequestDto.getPtwTestResultsDtoList()) {
//					ptwTestResultsDao.insertPtwTestResults(permitNumber, i);
//				}
//
//			}
//			// insert ptwcloseout
//			if (createRequestDto.getPtwCwpWorkTypeDto() != null && isCwp == true) {
//				ptwCwpWorkTypeDao.insertPtwCwpWorkType(permitNumber, createRequestDto.getPtwCwpWorkTypeDto());
//			}
//			if (createRequestDto.getPtwHwpWorkTypeDto() != null && isHwp == true) {
//				ptwHwpWorkTypeDao.insertPtwHwpWorkType(permitNumber, createRequestDto.getPtwHwpWorkTypeDto());
//			}
//			if (createRequestDto.getPtwCseWorkTypeDto() != null && isCse == true) {
//				ptwCseWorkTypeDao.insertPtwCseWorkType(permitNumber, createRequestDto.getPtwCseWorkTypeDto());
//			}
			// update ptwheader

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("JsaUpdateService || jsaUpdateService: " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage("Update JSA Service Error :" + e.getMessage());

		}

		logger.info("JsaUpdateService || jsaUpdateService: " + responseDto);

		return responseDto;
	}

}
