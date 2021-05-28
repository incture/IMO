package com.incture.iopptw.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.iopptw.dtos.CreateRequestDto;
import com.incture.iopptw.dtos.JsaLocationDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.dtos.PtwApprovalDto;
import com.incture.iopptw.dtos.PtwCloseOutDto;
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
import com.incture.iopptw.repositories.PtwApprovalDao;
import com.incture.iopptw.repositories.PtwCloseOutDao;
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
	private PtwApprovalDao ptwApprovalDao;
	@Autowired
	private PtwCloseOutDao ptwCloseOutDao;

	public ResponseDto updateJsaService(CreateRequestDto createRequestDto) {
		logger.info("JsaUpdateService || updateJsaService || createDto: " + createRequestDto);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			boolean isCwp = false;
			boolean isCse = false;
			boolean isHwp = false;
			boolean ptwConditionChk = false;
			int indexCWP = 0, ptwCWP = 0;
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
			if (createRequestDto.getJsaHazardsHeightsDto() != null) {
				jsaHazardsHeightsDao.updateJsaHazardsHeights(createRequestDto.getJsaHazardsHeightsDto());
			}
			if (createRequestDto.getJsaHazardsElectricalDto() != null) {
				jsaHazardsElectricalDao.updateJsaHazardsElectrical(createRequestDto.getJsaHazardsElectricalDto());
			}
			if (createRequestDto.getJsaHazardsMovingDto() != null) {
				jsaHazardsMovingDao.updateJsaHazardsMoving(createRequestDto.getJsaHazardsMovingDto());
			}
			if (createRequestDto.getJsaHazardsManualDto() != null) {
				jsaHazardsManualDao.updateJsaHazardsManual(createRequestDto.getJsaHazardsManualDto());
			}
			if (createRequestDto.getJsaHazardsToolsDto() != null) {
				jsaHazardsToolsDao.updateJsaHazardsTools(createRequestDto.getJsaHazardsToolsDto());
			}
			if (createRequestDto.getJsaHazardsFallsDto() != null) {
				jsaHazardsFallsDao.updateJsaHazardsFalls(createRequestDto.getJsaHazardsFallsDto());
			}
			if (createRequestDto.getJsaHazardsVoltageDto() != null) {
				jsaHazardsVoltageDao.updateJsaHazardsVoltage(createRequestDto.getJsaHazardsVoltageDto());
			}
			if (createRequestDto.getJsaHazardsExcavationdDto() != null) {
				jsaHazardsExcavationdDao.updateJsaHazardsExcavation(createRequestDto.getJsaHazardsExcavationdDto());
			}
			if (createRequestDto.getJsaHazardsMobileDto() != null) {
				jsaHazardsMobileDao.updateJsaHazardsMobile(createRequestDto.getJsaHazardsMobileDto());
			}
			if (createRequestDto.getJsaStepsDtoList() != null || !createRequestDto.getJsaStepsDtoList().isEmpty()) {
				jsaStepsDao.deleteJsaSteps(permitNumber.toString());
				for (JsaStepsDto i : createRequestDto.getJsaStepsDtoList()) {
					jsaStepsDao.insertJsaSteps(permitNumber.toString(), i);
				}
			}

			if (createRequestDto.getJsaStopTriggerDtoList() != null
					|| !createRequestDto.getJsaStopTriggerDtoList().isEmpty()) {
				jsaStopTriggerDao.deleteJsaStopTrigger(permitNumber.toString());
				for (JsaStopTriggerDto i : createRequestDto.getJsaStopTriggerDtoList()) {
					jsaStopTriggerDao.insertJsaStopTrigger(permitNumber.toString(), i);
				}

			}
			if (createRequestDto.getJsaLocationDtoList() != null
					|| !createRequestDto.getJsaLocationDtoList().isEmpty()) {
				jsaLocationDao.deleteJsaLocation(permitNumber.toString());
				for (JsaLocationDto i : createRequestDto.getJsaLocationDtoList()) {
					jsaLocationDao.insertJsaLocation(permitNumber.toString(), i);
				}

			}
			if (createRequestDto.getPtwPeopleDtoList() != null || !createRequestDto.getPtwPeopleDtoList().isEmpty()) {
				ptwPeopleDao.deletePtwPeople(permitNumber.toString());
				for (PtwPeopleDto i : createRequestDto.getPtwPeopleDtoList()) {
					ptwPeopleDao.insertPtwPeople(permitNumber.toString(), i);
				}

			}
			List<String> ptwPermitNumberList = new ArrayList<>();
			if (createRequestDto.getPtwHeaderDtoList() != null || !createRequestDto.getPtwHeaderDtoList().isEmpty()) {
				int index = 0;
				for (PtwHeaderDto i : createRequestDto.getPtwHeaderDtoList()) {
					String ptwHeader = "";
					ptwConditionChk = false;
					if (i.getIsCWP() == 1) {
						ptwHeader = "CWP" + permitNumber;
						isCwp = true;
						ptwCWP = index;
						ptwConditionChk = true;
						ptwPermitNumberList.add(ptwHeader);

					} else if (i.getIsHWP() == 1) {
						ptwHeader = "HWP" + permitNumber;
						ptwConditionChk = true;
						isHwp = true;
						ptwPermitNumberList.add(ptwHeader);
					} else if (i.getIsCSE() == 1) {
						ptwHeader = "CSE" + permitNumber;
						isCse = true;
						ptwConditionChk = true;
						ptwPermitNumberList.add(ptwHeader);
					}
					index++;
					if (ptwConditionChk) {
						ptwHeaderDao.insertPtwHeader(permitNumber.toString(), ptwHeader, i);
					}

				}

			}
			if (ptwConditionChk == true) {

				if (createRequestDto.getPtwRequiredDocumentDtoList() != null
						|| !createRequestDto.getPtwRequiredDocumentDtoList().isEmpty()) {
					for (PtwRequiredDocumentDto i : createRequestDto.getPtwRequiredDocumentDtoList()) {
						ptwRequiredDocumentDao.insertPtwRequiredDocument(permitNumber.toString(), i);
					}

				}
				if (createRequestDto.getPtwApprovalDtoList() != null
						|| !createRequestDto.getPtwApprovalDtoList().isEmpty()) {
					for (PtwApprovalDto i : createRequestDto.getPtwApprovalDtoList()) {
						ptwApprovalDao.insertPtwApproval(permitNumber.toString(), i);
					}

				}
				if (createRequestDto.getPtwApprovalDtoList() != null
						&& !createRequestDto.getPtwApprovalDtoList().isEmpty()) {
					int i = 0;
					for (PtwApprovalDto ptwApprovalDto : createRequestDto.getPtwApprovalDtoList()) {
						if (ptwApprovalDto.getIsCWP() == 1) {
							indexCWP = i;
						}
						i++;
						ptwApprovalDao.insertPtwApproval(permitNumber.toString(), ptwApprovalDto);
					}
				}

				if (createRequestDto.getPtwTestRecordDto() != null) {
					ptwTestRecordDao.deletePtwTestRecord(permitNumber.toString());
					ptwTestRecordDao.insertPtwTestRecord(permitNumber.toString(),
							createRequestDto.getPtwTestRecordDto());
				}

				if (createRequestDto.getPtwTestResultsDtoList() != null
						|| !createRequestDto.getPtwTestResultsDtoList().isEmpty()) {
					ptwTestResultsDao.deletePtwTestResults(permitNumber.toString());
					for (PtwTestResultsDto i : createRequestDto.getPtwTestResultsDtoList()) {
						ptwTestResultsDao.insertPtwTestResults(permitNumber.toString(), i);
					}

				}
				if (createRequestDto.getPtwCloseOutDtoList() != null
						&& !createRequestDto.getPtwCloseOutDtoList().isEmpty()) {
					for (PtwCloseOutDto ptwCloseOutDto : createRequestDto.getPtwCloseOutDtoList()) {
						ptwCloseOutDao.insertPtwCloseOut(ptwCloseOutDto);
					}
				}
				if (createRequestDto.getPtwCwpWorkTypeDto() != null && isCwp == true) {
					ptwCwpWorkTypeDao.insertPtwCwpWorkType(permitNumber.toString(),
							createRequestDto.getPtwCwpWorkTypeDto());
				}
				if (createRequestDto.getPtwHwpWorkTypeDto() != null && isHwp == true) {
					ptwHwpWorkTypeDao.insertPtwHwpWorkType(permitNumber.toString(),
							createRequestDto.getPtwHwpWorkTypeDto());
				}
				if (createRequestDto.getPtwCseWorkTypeDto() != null && isCse == true) {
					ptwCseWorkTypeDao.insertPtwCseWorkType(permitNumber.toString(),
							createRequestDto.getPtwCseWorkTypeDto());
				}
				if (isCwp == true) {
					ptwHeaderDao.updatePtwHeader(createRequestDto.getPtwApprovalDtoList().get(indexCWP),
							createRequestDto.getPtwHeaderDtoList().get(ptwCWP));
				}
			}
			responseDto.setData("JSA "+permitNumber+" updated successfully");

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
