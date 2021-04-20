package com.incture.ptw.services;

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
import com.incture.ptw.dao.PtwCseWorkTypeDao;
import com.incture.ptw.dao.PtwCwpWorkTypeDao;
import com.incture.ptw.dao.PtwHeaderDao;
import com.incture.ptw.dao.PtwHwpWorkTypeDao;
import com.incture.ptw.dao.PtwPeopleDao;
import com.incture.ptw.dao.PtwRequiredDocumentDao;
import com.incture.ptw.dao.PtwTestRecordDao;
import com.incture.ptw.dao.PtwTestResultsDao;
import com.incture.ptw.dto.CreateRequestDto;
import com.incture.ptw.dto.JsaHazardsDroppedDto;
import com.incture.ptw.dto.JsaHazardsElectricalDto;
import com.incture.ptw.dto.JsaHazardsExcavationdDto;
import com.incture.ptw.dto.JsaHazardsFallsDto;
import com.incture.ptw.dto.JsaHazardsHeightsDto;
import com.incture.ptw.dto.JsaHazardsHighNoiseDto;
import com.incture.ptw.dto.JsaHazardsIgnitionDto;
import com.incture.ptw.dto.JsaHazardsLiftingDto;
import com.incture.ptw.dto.JsaHazardsManualDto;
import com.incture.ptw.dto.JsaHazardsMobileDto;
import com.incture.ptw.dto.JsaHazardsMovingDto;
import com.incture.ptw.dto.JsaHazardsSimultaneousDto;
import com.incture.ptw.dto.JsaHazardsSpillsDto;
import com.incture.ptw.dto.JsaHazardsSubstancesDto;
import com.incture.ptw.dto.JsaHazardsToolsDto;
import com.incture.ptw.dto.JsaHazardsVoltageDto;
import com.incture.ptw.dto.JsaHazardsWeatherDto;
import com.incture.ptw.dto.JsaLocationDto;
import com.incture.ptw.dto.JsaStepsDto;
import com.incture.ptw.dto.JsaStopTriggerDto;
import com.incture.ptw.util.ResponseDto;

@Service
public class CreateService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JsaHeaderDao jsaHeaderDao;
	@Autowired
	private PtwCwpWorkTypeDao ptwCwpWorkTypeDao;
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
	private JsaHazardsPersonnelDao jsaHazardsPersonnel;
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
	private PtwCwpWorkTypeDao ptWorkTypeDao;
	@Autowired
	private PtwHwpWorkTypeDao ptwHwpWorkTypeDao;
	@Autowired
	private PtwCseWorkTypeDao ptwCseWorkTypeDao;
	@Autowired
	private JsaHeaderDao jsHeaderDao;

	public ResponseDto createService(CreateRequestDto createRequestDto) {
		logger.info("CreateService || createService || createDto: " + createRequestDto);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatus(Boolean.TRUE);
		responseDto.setStatusCode(200);
		try {
			if (createRequestDto.getJsaheaderDto() != null) {
				jsaHeaderDao.insertJsaHeader(createRequestDto.getJsaheaderDto());
			}

			responseDto.setData("Success:   approved succesfully.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CreateService || createService: " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			responseDto.setStatus(Boolean.FALSE);
			responseDto.setStatusCode(500);
			responseDto.setMessage(e.getMessage());

		}

		logger.info("CreateService || createService: " + responseDto);

		return responseDto;

	}

}
