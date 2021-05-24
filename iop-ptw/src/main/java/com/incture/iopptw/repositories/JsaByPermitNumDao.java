package com.incture.iopptw.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.iopptw.dtos.GetJsaByPermitNumPayloadDto;
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
import com.incture.iopptw.dtos.JsaLocationDto;
import com.incture.iopptw.dtos.JsaReviewDto;
import com.incture.iopptw.dtos.JsaRiskAssesmentDto;
import com.incture.iopptw.dtos.JsaStepsDto;
import com.incture.iopptw.dtos.JsaStopTriggerDto;
import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.dtos.JsappeDto;
import com.incture.iopptw.dtos.PtwPeopleDto;

@Repository("JsaByPermitNumDao")
public class JsaByPermitNumDao extends BaseDao {

	@Autowired
	private JsaHeaderDao jsaHeaderDao;

	@Autowired
	private JsaReviewDao jsaReviewDao;

	@Autowired
	private JsaRiskAssessmentDao jsaRiskAssessmentDao;

	@Autowired
	private JsappeDao jsappeDao;

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
	private PtwPeopleDao ptwPeopleDao;

	public GetJsaByPermitNumPayloadDto getJsaByPermitNum(String permitNumber) {
		GetJsaByPermitNumPayloadDto getJsaByPermitNumPayloadDto = new GetJsaByPermitNumPayloadDto();
		// CreateRequestDto payload = new CreateRequestDto();
		// List<Object[]> obj;
		Integer permitNum;

		try {
			JsaheaderDto jsaheaderDto = jsaHeaderDao.getJsaHeader(permitNumber);
			permitNum = jsaheaderDto.getPermitNumber();
			logger.info("JsaByPermitNumDao | permitNum" + permitNum);
			logger.info("JsaByPermitNumDao | jsaheaderDto" + jsaheaderDto.toString());
			getJsaByPermitNumPayloadDto.setJsaheaderDto(jsaheaderDto);
			////////////////////////////////////////////////////////////

			JsaReviewDto jsaReviewDto = jsaReviewDao.getJsaReview(permitNumber);
			logger.info("JsaByPermitNumDao | jsaReviewDto" + jsaReviewDto.toString());
			getJsaByPermitNumPayloadDto.setJsaReviewDto(jsaReviewDto);

			////////////////////////////////////////////////////////////

			JsaRiskAssesmentDto jsaRiskAssesmentDto = jsaRiskAssessmentDao.getJsaRiskAss(permitNumber);
			logger.info("JsaByPermitNumDao | jsaRiskAssesmentDto" + jsaRiskAssesmentDto.toString());
			getJsaByPermitNumPayloadDto.setJsaRiskAssesmentDto(jsaRiskAssesmentDto);

			////////////////////////////////////////////////////////////

			JsappeDto jsappeDto = jsappeDao.getJsappe(permitNumber);
			logger.info("JsaByPermitNumDao | jsappeDto" + jsappeDto.toString());
			getJsaByPermitNumPayloadDto.setJsappeDto(jsappeDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsPressurizedDto jsaHazardsPressurizedDto = jsaHazardsPressurizedDao
					.getjsaHazardsPress(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsPressurizedDto" + jsaHazardsPressurizedDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsPressurizedDto(jsaHazardsPressurizedDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsVisibilityDto jsaHazardsVisibilityDto = jsaHazardsVisibilityDao
					.getJsaHazardsVisible(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsVisibilityDto" + jsaHazardsVisibilityDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsVisibilityDto(jsaHazardsVisibilityDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsPersonnelDto jsaHazardsPersonnelDto = jsaHazardsPersonnelDao.getJsaPersonnel(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsPersonnelDto" + jsaHazardsPersonnelDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsPersonnelDto(jsaHazardsPersonnelDto);

			/////////////////////////////////////////////////////////////

			JsaHazardscseDto jsaHazardscseDto = jsaHazardsCseDao.getJsaHazardsCse(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardscseDto" + jsaHazardscseDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardscseDto(jsaHazardscseDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = jsaHazardsSimultaneousDao
					.getJsaHazardsSimultan(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsSimultaneousDto" + jsaHazardsSimultaneousDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsSimultaneousDto(jsaHazardsSimultaneousDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsIgnitionDto jsaHazardsIgnitionDto = jsaHazardsIgnitionDao.getJsaHazardsIgnition(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsSimultaneousDto" + jsaHazardsSimultaneousDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsIgnitionDto(jsaHazardsIgnitionDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsSubstancesDto jsaHazardsSubstancesDto = jsaHazardsSubstancesDao
					.getJsaHazardsSubstances(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsSubstancesDto" + jsaHazardsSubstancesDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsSubstancesDto(jsaHazardsSubstancesDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsSpillsDto jsaHazardsSpillsDto = jsaHazardsSpillsDao.getJsaHazardsSpillsDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsSpillsDto" + jsaHazardsSpillsDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsSpillsDto(jsaHazardsSpillsDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsWeatherDto jsaHazardsWeatherDto = jsaHazardsWeatherDao.getJsaHazardsWeatherDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsWeatherDto" + jsaHazardsWeatherDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsWeatherDto(jsaHazardsWeatherDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto = jsaHazardsHighNoiseDao
					.getJsaHazardsHighNoiseDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsHighNoiseDto" + jsaHazardsHighNoiseDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsHighNoiseDto(jsaHazardsHighNoiseDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsDroppedDto jsaHazardsDroppedDto = jsaHazardsDroppedDao.getJsaHazardsDroppedDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsDroppedDto" + jsaHazardsDroppedDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsDroppedDto(jsaHazardsDroppedDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsLiftingDto jsaHazardsLiftingDto = jsaHazardsLiftingDao.getJsaHazardsLiftingDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsLiftingDto" + jsaHazardsLiftingDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsLiftingDto(jsaHazardsLiftingDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsHeightsDto jsaHazardsHeightsDto = jsaHazardsHeightsDao.getJsaHazardsHeightsDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsHeightsDto" + jsaHazardsHeightsDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsHeightsDto(jsaHazardsHeightsDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsElectricalDto jsaHazardsElectricalDto = jsaHazardsElectricalDao
					.getJsaHazardsElectricalDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsElectricalDto" + jsaHazardsElectricalDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsElectricalDto(jsaHazardsElectricalDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsMovingDto jsaHazardsMovingDto = jsaHazardsMovingDao.getJsaHazardsMovingDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsMovingDto" + jsaHazardsMovingDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsMovingDto(jsaHazardsMovingDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsManualDto jsaHazardsManualDto = jsaHazardsManualDao.getJsaHazardsManualDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsManualDto" + jsaHazardsManualDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsManualDto(jsaHazardsManualDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsToolsDto jsaHazardsToolsDto = jsaHazardsToolsDao.getJsaHazardsToolsDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsToolsDto" + jsaHazardsToolsDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsToolsDto(jsaHazardsToolsDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsFallsDto jsaHazardsFallsDto = jsaHazardsFallsDao.getJsaHazardsFallsDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsFallsDto" + jsaHazardsFallsDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsFallsDto(jsaHazardsFallsDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsVoltageDto jsaHazardsVoltageDto = jsaHazardsVoltageDao.getJsaHazardsVoltageDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsVoltageDto" + jsaHazardsVoltageDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsVoltageDto(jsaHazardsVoltageDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsExcavationdDto jsaHazardsExcavationdDto = jsaHazardsExcavationdDao
					.getJsaHazardsExcavationdDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsExcavationdDto" + jsaHazardsExcavationdDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsExcavationdDto(jsaHazardsExcavationdDto);

			/////////////////////////////////////////////////////////////

			JsaHazardsMobileDto jsaHazardsMobileDto = jsaHazardsMobileDao.getJsaHazardsMobileDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaHazardsMobileDto" + jsaHazardsMobileDto.toString());
			getJsaByPermitNumPayloadDto.setJsaHazardsMobileDto(jsaHazardsMobileDto);

			/////////////////////////////////////////////////////////////

			List<JsaStepsDto> jsaStepsDtoList = jsaStepsDao.getJsaStepsDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaStepsDtoList" + jsaStepsDtoList.toString());
			getJsaByPermitNumPayloadDto.setJsaStepsDtoList(jsaStepsDtoList);

			/////////////////////////////////////////////////////////////

			List<JsaStopTriggerDto> jsaStopTriggerDtoList = jsaStopTriggerDao.getJsaStopTriggerDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaStopTriggerDtoList" + jsaStopTriggerDtoList.toString());
			getJsaByPermitNumPayloadDto.setJsaStopTriggerDtoList(jsaStopTriggerDtoList);

			/////////////////////////////////////////////////////////////

			List<JsaLocationDto> jsaLocationDtoList = jsaLocationDao.getJsaLocationDto(permitNumber);
			logger.info("JsaByPermitNumDao | jsaLocationDtoList" + jsaLocationDtoList.toString());
			getJsaByPermitNumPayloadDto.setJsaLocationDtoList(jsaLocationDtoList);

			/////////////////////////////////////////////////////////////

			List<PtwPeopleDto> ptwPeopleDtoList = ptwPeopleDao.getPtwPeople(permitNumber);
			logger.info("JsaByPermitNumDao | ptwPeopleDtoList" + ptwPeopleDtoList.toString());
			getJsaByPermitNumPayloadDto.setPtwPeopleDtoList(ptwPeopleDtoList);

			/////////////////////////////////////////////////////////////
			return getJsaByPermitNumPayloadDto;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
		// try {
		// String sql = "select distinct PERMITNUMBER,
		// JSAPERMITNUMBER,HASCWP,HASHWP, "
		// + "
		// HASCSE,TASKDESCRIPTION,IDENTIFYMOSTSERIOUSPOTENTIALINJURY,ISACTIVE,STATUS
		// from "
		// + " IOP.JSAHEADER where PERMITNUMBER = :permitNumber";
		// logger.info("JSAHEADER sql " + sql);
		// Query q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNumber", permitNumber);
		// obj = q.getResultList();
		// JsaheaderDto jsaheaderDto = new JsaheaderDto();
		// for (Object[] a : obj) {
		// jsaheaderDto.setPermitNumber((Integer) a[0]);
		// jsaheaderDto.setJsaPermitNumber((String) a[1]);
		// jsaheaderDto.setHasCWP(Integer.parseInt(a[2].toString()));
		// jsaheaderDto.setHasHWP(Integer.parseInt(a[2].toString()));
		// jsaheaderDto.setHasCSE(Integer.parseInt(a[2].toString()));
		// jsaheaderDto.setTaskDescription((String) a[5]);
		// jsaheaderDto.setIdentifyMostSeriousPotentialInjury((String) a[6]);
		// jsaheaderDto.setIsActive(Integer.parseInt(a[2].toString()));
		// jsaheaderDto.setStatus((String) a[8]);
		// }
		//
		// logger.info(jsaheaderDto.toString());
		// payload.setJsaheaderDto(jsaheaderDto);
		// getJsaByPermitNumPayloadDto.setJsaheaderDto(jsaheaderDto);
		// ////////////////////////////////////////////////////////////
		//
		// Integer permitNum = jsaheaderDto.getPermitNumber(); // as per xsjs
		// // logic and
		// // this permit
		// // number is
		// // used
		// // everywhere
		// sql = "select distinct PERMITNUMBER,
		// CREATEDBY,CREATEDDATE,APPROVEDBY,APPROVEDDATE,LASTUPDATEDBY, "
		// + " LASTUPDATEDDATE from IOP.JSAREVIEW where PERMITNUMBER =
		// :permitNum";
		// logger.info("JSAREVIEW sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaReviewDto jsaReviewDto = new JsaReviewDto();
		// for (Object[] a : obj) {
		// jsaReviewDto.setPermitNumber((Integer) a[0]);
		// jsaReviewDto.setCreatedBy((String) a[1]);
		// jsaReviewDto.setCreatedDate((Date) a[2]);
		// jsaReviewDto.setApprovedBy((String) a[3]);
		// jsaReviewDto.setApprovedDate((Date) a[4]);
		// jsaReviewDto.setLastUpdatedBy((String) a[5]);
		// jsaReviewDto.setLastUpdatedDate((Date) a[6]);
		// }
		// logger.info(jsaReviewDto.toString());
		// payload.setJsaReviewDto(jsaReviewDto);
		// getJsaByPermitNumPayloadDto.setJsaReviewDto(jsaReviewDto);
		// ///////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// MUSTMODIFYEXISTINGWORKPRACTICE,HASCONTINUEDRISK "
		// + " from IOP.JSARISKASSESMENT where PERMITNUMBER = :permitNum";
		// logger.info("JSARISKASSESMENT sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaRiskAssesmentDto jsaRiskAssesmentDto = new JsaRiskAssesmentDto();
		// for (Object[] a : obj) {
		// jsaRiskAssesmentDto.setPermitNumber((Integer) a[0]);
		// jsaRiskAssesmentDto.setMustModifyExistingWorkPractice(Integer.parseInt(a[1].toString()));
		// jsaRiskAssesmentDto.setHasContinuedRisk(Integer.parseInt(a[2].toString()));
		// }
		// logger.info(jsaRiskAssesmentDto.toString());
		// payload.setJsaRiskAssesmentDto(jsaRiskAssesmentDto);
		// getJsaByPermitNumPayloadDto.setJsaRiskAssesmentDto(jsaRiskAssesmentDto);
		// /////////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// HARDHAT,SAFETYBOOT,GOGGLES,FACESHIELD,SAFETYGLASSES, "
		// + "
		// SINGLEEAR,DOUBLEEARS,RESPIRATORTYPEDESCRIPTION,NEEDSCBA,NEEDDUSTMASK,COTTONGLOVE,
		// "
		// + "
		// LEATHERGLOVE,IMPACTPROTECTION,GLOVEDESCRIPTION,CHEMICALGLOVEDESCRIPTION,FALLPROTECTION,
		// "
		// + "
		// FALLRESTRAINT,CHEMICALSUIT,APRON,FLAMERESISTANTCLOTHING,OTHERPPEDESCRIPTION,
		// "
		// + " NEEDFOULWEATHERGEAR,HAVECONSENTOFTASKLEADER,COMPANYOFTASKLEADER
		// from IOP.JSA_PPE "
		// + " where PERMITNUMBER = :permitNum";
		// logger.info("JSA_PPE sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsappeDto jsappeDto = new JsappeDto();
		// for (Object[] a : obj) {
		// jsappeDto.setPermitNumber((Integer) a[0]);
		// jsappeDto.setHardHat(Integer.parseInt(a[1].toString()));
		// jsappeDto.setSafetyBoot(Integer.parseInt(a[2].toString()));
		// jsappeDto.setGoggles(Integer.parseInt(a[3].toString()));
		// jsappeDto.setFaceShield(Integer.parseInt(a[4].toString()));
		// jsappeDto.setSafetyGlasses(Integer.parseInt(a[5].toString()));
		// jsappeDto.setSingleEar(Integer.parseInt(a[6].toString()));
		// jsappeDto.setDoubleEars(Integer.parseInt(a[7].toString()));
		// jsappeDto.setRespiratorTypeDescription((String) a[8]);
		// jsappeDto.setNeedSCBA(Integer.parseInt(a[9].toString()));
		// jsappeDto.setNeedDustMask(Integer.parseInt(a[10].toString()));
		// jsappeDto.setCottonGlove(Integer.parseInt(a[11].toString()));
		// jsappeDto.setLeatherGlove(Integer.parseInt(a[12].toString()));
		// jsappeDto.setImpactProtection(Integer.parseInt(a[13].toString()));
		// jsappeDto.setGloveDescription((String) a[14]);
		// jsappeDto.setChemicalGloveDescription((String) a[15]);
		// jsappeDto.setFallProtection(Integer.parseInt(a[16].toString()));
		// jsappeDto.setFallRestraint(Integer.parseInt(a[17].toString()));
		// jsappeDto.setChemicalSuit(Integer.parseInt(a[18].toString()));
		// jsappeDto.setApron(Integer.parseInt(a[19].toString()));
		// jsappeDto.setFlameResistantClothing(Integer.parseInt(a[20].toString()));
		// jsappeDto.setOtherPPEDescription((String) a[21]);
		// jsappeDto.setNeedFoulWeatherGear((String) a[22]); // need to
		// // convert
		// // this into
		// // string as
		// // per xsjs
		// // payload
		// jsappeDto.setHaveConsentOfTaskLeader(Integer.parseInt(a[23].toString()));
		// jsappeDto.setCompanyOfTaskLeader((String) a[24]);
		// }
		// logger.info(jsappeDto.toString());
		// payload.setJsappeDto(jsappeDto);
		// getJsaByPermitNumPayloadDto.setJsappeDto(jsappeDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// PRESURIZEDEQUIPMENT,PERFORMISOLATION,DEPRESSURIZEDRAIN, "
		// + "
		// RELIEVETRAPPEDPRESSURE,DONOTWORKINLINEOFFIRE,ANTICIPATERESIDUAL,SECUREALLHOSES
		// "
		// + " from IOP.JSAHAZARDSPRESSURIZED where PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSPRESSURIZED sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsPressurizedDto jsaHazardsPressurizedDto = new
		// JsaHazardsPressurizedDto();
		// for (Object[] a : obj) {
		// jsaHazardsPressurizedDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsPressurizedDto.setPresurizedEquipment(Integer.parseInt(a[1].toString()));
		// jsaHazardsPressurizedDto.setPerformIsolation(Integer.parseInt(a[2].toString()));
		// jsaHazardsPressurizedDto.setDepressurizeDrain(Integer.parseInt(a[3].toString()));
		// jsaHazardsPressurizedDto.setRelieveTrappedPressure(Integer.parseInt(a[4].toString()));
		// jsaHazardsPressurizedDto.setDoNotWorkInLineOfFire(Integer.parseInt(a[5].toString()));
		// jsaHazardsPressurizedDto.setAnticipateResidual(Integer.parseInt(a[6].toString()));
		// jsaHazardsPressurizedDto.setSecureAllHoses(Integer.parseInt(a[7].toString()));
		// }
		// payload.setJsaHazardsPressurizedDto(jsaHazardsPressurizedDto);
		// logger.info(jsaHazardsPressurizedDto.toString());
		// getJsaByPermitNumPayloadDto.setJsaHazardsPressurizedDto(jsaHazardsPressurizedDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// POORLIGHTING,PROVIDEALTERNATELIGHTING, "
		// + "
		// WAITUNTILVISIBILITYIMPROVE,DEFERUNTILVISIBILITYIMPROVE,KNOWDISTANCEFROMPOLES
		// "
		// + " from IOP.JSAHAZARDSVISIBILITY where PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSVISIBILITY sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsVisibilityDto jsaHazardsVisibilityDto = new
		// JsaHazardsVisibilityDto();
		// for (Object[] a : obj) {
		// jsaHazardsVisibilityDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsVisibilityDto.setPoorLighting(Integer.parseInt(a[1].toString()));
		// jsaHazardsVisibilityDto.setProvideAlternateLighting(Integer.parseInt(a[2].toString()));
		// jsaHazardsVisibilityDto.setWaitUntilVisibilityImprove(Integer.parseInt(a[3].toString()));
		// jsaHazardsVisibilityDto.setDeferUntilVisibilityImprove(Integer.parseInt(a[4].toString()));
		// jsaHazardsVisibilityDto.setKnowDistanceFromPoles(Integer.parseInt(a[5].toString()));
		// }
		// payload.setJsaHazardsVisibilityDto(jsaHazardsVisibilityDto);
		// logger.info(jsaHazardsVisibilityDto.toString());
		// getJsaByPermitNumPayloadDto.setJsaHazardsVisibilityDto(jsaHazardsVisibilityDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// PERSONNEL,PERFORMINDUCTION,MENTORCOACHSUPERVISE, "
		// + "
		// VERIFYCOMPETENCIES,ADDRESSLIMITATIONS,MANAGELANGUAGEBARRIERS,WEARSEATBELTS
		// "
		// + " from IOP.JSAHAZARDSPERSONNEL where PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSPERSONNEL sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsPersonnelDto jsaHazardsPersonnelDto = new
		// JsaHazardsPersonnelDto();
		// for (Object[] a : obj) {
		// jsaHazardsPersonnelDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsPersonnelDto.setPersonnel(Integer.parseInt(a[1].toString()));
		// jsaHazardsPersonnelDto.setPerformInduction(Integer.parseInt(a[2].toString()));
		// jsaHazardsPersonnelDto.setMentorCoachSupervise(Integer.parseInt(a[3].toString()));
		// jsaHazardsPersonnelDto.setVerifyCompetencies(Integer.parseInt(a[4].toString()));
		// jsaHazardsPersonnelDto.setAddressLimitations(Integer.parseInt(a[5].toString()));
		// jsaHazardsPersonnelDto.setManageLanguageBarriers(Integer.parseInt(a[6].toString()));
		// jsaHazardsPersonnelDto.setWearSeatBelts(Integer.parseInt(a[7].toString()));
		// }
		// payload.setJsaHazardsPersonnelDto(jsaHazardsPersonnelDto);
		// logger.info(jsaHazardsPersonnelDto.toString());
		// getJsaByPermitNumPayloadDto.setJsaHazardsPersonnelDto(jsaHazardsPersonnelDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// CONFINEDSPACEENTRY,DISCUSSWORKPRACTICE, "
		// + "
		// CONDUCTATMOSPHERICTESTING,MONITORACCESS,PROTECTSURFACES,PROHIBITMOBILEENGINE,
		// "
		// + " PROVIDEOBSERVER,DEVELOPRESCUEPLAN from IOP.JSAHAZARDSCSE where
		// PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSCSE sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardscseDto jsaHazardscseDto = new JsaHazardscseDto();
		// for (Object[] a : obj) {
		// jsaHazardscseDto.setPermitNumber((Integer) a[0]);
		// jsaHazardscseDto.setConfinedSpaceEntry(Integer.parseInt(a[1].toString()));
		// jsaHazardscseDto.setDiscussWorkPractice(Integer.parseInt(a[2].toString()));
		// jsaHazardscseDto.setConductAtmosphericTesting(Integer.parseInt(a[3].toString()));
		// jsaHazardscseDto.setMonitorAccess(Integer.parseInt(a[4].toString()));
		// jsaHazardscseDto.setProtectSurfaces(Integer.parseInt(a[5].toString()));
		// jsaHazardscseDto.setProhibitMobileEngine(Integer.parseInt(a[6].toString()));
		// jsaHazardscseDto.setProvideObserver(Integer.parseInt(a[7].toString()));
		// jsaHazardscseDto.setDevelopRescuePlan(Integer.parseInt(a[8].toString()));
		// }
		// payload.setJsaHazardscseDto(jsaHazardscseDto);
		// logger.info(jsaHazardscseDto.toString());
		// getJsaByPermitNumPayloadDto.setJsaHazardscseDto(jsaHazardscseDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// SIMULTANEOUSOPERATIONS,FOLLOWSIMOPSMATRIX, "
		// + "
		// MOCREQUIREDFOR,INTERFACEBETWEENGROUPS,USEBARRIERSAND,HAVEPERMITSIGNED
		// "
		// + " from IOP.JSAHAZARDSSIMULTANEOUS where PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSSIMULTANEOUS sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = new
		// JsaHazardsSimultaneousDto();
		// for (Object[] a : obj) {
		// jsaHazardsSimultaneousDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsSimultaneousDto.setSimultaneousOperations(Integer.parseInt(a[1].toString()));
		// jsaHazardsSimultaneousDto.setFollowSimopsMatrix(Integer.parseInt(a[2].toString()));
		// jsaHazardsSimultaneousDto.setMocRequiredFor(Integer.parseInt(a[3].toString()));
		// jsaHazardsSimultaneousDto.setInterfaceBetweenGroups(Integer.parseInt(a[4].toString()));
		// jsaHazardsSimultaneousDto.setUseBarriersAnd(Integer.parseInt(a[5].toString()));
		// jsaHazardsSimultaneousDto.setHavePermitSigned(Integer.parseInt(a[6].toString()));
		// }
		// payload.setJsaHazardsSimultaneousDto(jsaHazardsSimultaneousDto);
		// logger.info(jsaHazardsSimultaneousDto.toString());
		// getJsaByPermitNumPayloadDto.setJsaHazardsSimultaneousDto(jsaHazardsSimultaneousDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct
		// PERMITNUMBER,IGNITIONSOURCES,REMOVECOMBUSTIBLEMATERIALS,PROVIDEFIREWATCH,
		// "
		// + "
		// IMPLEMENTABRASIVEBLASTINGCONTROLS,CONDUCTCONTINUOUSGASTESTING,EARTHFORSTATICELECTRICITY
		// "
		// + " from IOP.JSAHAZARDSIGNITION where PERMITNUMBER = :permitNum";
		// logger.info("JSAHAZARDSIGNITION sql " + sql);
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsIgnitionDto jsaHazardsIgnitionDto = new
		// JsaHazardsIgnitionDto();
		// for (Object[] a : obj) {
		// jsaHazardsIgnitionDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsIgnitionDto.setIgnitionSources(Integer.parseInt(a[1].toString()));
		// jsaHazardsIgnitionDto.setRemoveCombustibleMaterials(Integer.parseInt(a[2].toString()));
		// jsaHazardsIgnitionDto.setProvideFireWatch(Integer.parseInt(a[3].toString()));
		// jsaHazardsIgnitionDto.setImplementAbrasiveBlastingControls(Integer.parseInt(a[4].toString()));
		// jsaHazardsIgnitionDto.setConductContinuousGasTesting(Integer.parseInt(a[5].toString()));
		// jsaHazardsIgnitionDto.setEarthForStaticElectricity(Integer.parseInt(a[6].toString()));
		// }
		// logger.info(jsaHazardsIgnitionDto.toString());
		// payload.setJsaHazardsIgnitionDto(jsaHazardsIgnitionDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsIgnitionDto(jsaHazardsIgnitionDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// HAZARDOUSSUBSTANCES,DRAINEQUIPMENT,FOLLOWSDSCONTROLS, "
		// + " IMPLEMENTHEALTHHAZARDCONTROLS,TESTMATERIAL from
		// IOP.JSAHAZARDSSUBSTANCES "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsSubstancesDto jsaHazardsSubstancesDto = new
		// JsaHazardsSubstancesDto();
		// for (Object[] a : obj) {
		// jsaHazardsSubstancesDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsSubstancesDto.setHazardousSubstances(Integer.parseInt(a[1].toString()));
		// jsaHazardsSubstancesDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
		// jsaHazardsSubstancesDto.setFollowSdsControls(Integer.parseInt(a[3].toString()));
		// jsaHazardsSubstancesDto.setImplementHealthHazardControls(Integer.parseInt(a[4].toString()));
		// jsaHazardsSubstancesDto.setTestMaterial(Integer.parseInt(a[5].toString()));
		// }
		// logger.info(jsaHazardsSubstancesDto.toString());
		// payload.setJsaHazardsSubstancesDto(jsaHazardsSubstancesDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsSubstancesDto(jsaHazardsSubstancesDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// POTENTIALSPILLS,DRAINEQUIPMENT,CONNECTIONSINGOODCONDITION, "
		// + "
		// SPILLCONTAINMENTEQUIPMENT,HAVESPILLCLEANUPMATERIALS,RESTRAINHOSESWHENNOTINUSE
		// "
		// + " from IOP.JSAHAZARDSSPILLS where PERMITNUMBER = :permitNum";
		//
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsSpillsDto jsaHazardsSpillsDto = new JsaHazardsSpillsDto();
		// for (Object[] a : obj) {
		// jsaHazardsSpillsDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsSpillsDto.setPotentialSpills(Integer.parseInt(a[1].toString()));
		// jsaHazardsSpillsDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
		// jsaHazardsSpillsDto.setConnectionsInGoodCondition(Integer.parseInt(a[3].toString()));
		// jsaHazardsSpillsDto.setSpillContainmentEquipment(Integer.parseInt(a[4].toString()));
		// jsaHazardsSpillsDto.setHaveSpillCleanupMaterials(Integer.parseInt(a[5].toString()));
		// jsaHazardsSpillsDto.setRestrainHosesWhenNotInUse(Integer.parseInt(a[6].toString()));
		//
		// }
		// logger.info(jsaHazardsSpillsDto.toString());
		// payload.setJsaHazardsSpillsDto(jsaHazardsSpillsDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsSpillsDto(jsaHazardsSpillsDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// WEATHER,CONTROLSFORSLIPPERYSURFACE,HEATBREAK, "
		// + " COLDHEATERS,LIGHTNING from IOP.JSAHAZARDSWEATHER where
		// PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsWeatherDto jsaHazardsWeatherDto = new
		// JsaHazardsWeatherDto();
		// for (Object[] a : obj) {
		// jsaHazardsWeatherDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsWeatherDto.setWeather(Integer.parseInt(a[1].toString()));
		// jsaHazardsWeatherDto.setControlsForSlipperySurface(Integer.parseInt(a[2].toString()));
		// jsaHazardsWeatherDto.setHeatBreak(Integer.parseInt(a[3].toString()));
		// jsaHazardsWeatherDto.setColdHeaters(Integer.parseInt(a[4].toString()));
		// jsaHazardsWeatherDto.setLightning(Integer.parseInt(a[5].toString()));
		// }
		// logger.info(jsaHazardsWeatherDto.toString());
		// payload.setJsaHazardsWeatherDto(jsaHazardsWeatherDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsWeatherDto(jsaHazardsWeatherDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// HIGHNOISE,WEARCORRECTHEARING,MANAGEEXPOSURETIMES, "
		// + "
		// SHUTDOWNEQUIPMENT,USEQUIETTOOLS,SOUNDBARRIERS,PROVIDESUITABLECOMMS "
		// + " from IOP.JSAHAZARDSHIGHNOISE where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto = new
		// JsaHazardsHighNoiseDto();
		// for (Object[] a : obj) {
		// jsaHazardsHighNoiseDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsHighNoiseDto.setHighNoise(Integer.parseInt(a[1].toString()));
		// jsaHazardsHighNoiseDto.setWearCorrectHearing(Integer.parseInt(a[2].toString()));
		// jsaHazardsHighNoiseDto.setManageExposureTimes(Integer.parseInt(a[3].toString()));
		// jsaHazardsHighNoiseDto.setShutDownEquipment(Integer.parseInt(a[4].toString()));
		// jsaHazardsHighNoiseDto.setUseQuietTools(Integer.parseInt(a[5].toString()));
		// jsaHazardsHighNoiseDto.setSoundBarriers(Integer.parseInt(a[6].toString()));
		// jsaHazardsHighNoiseDto.setProvideSuitableComms(Integer.parseInt(a[7].toString()));
		// }
		// logger.info(jsaHazardsHighNoiseDto.toString());
		// payload.setJsaHazardsHighNoiseDto(jsaHazardsHighNoiseDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsHighNoiseDto(jsaHazardsHighNoiseDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// DROPPEDOBJECTS,MARKRESTRICTENTRY,USELIFTINGEQUIPMENTTORAISE, "
		// + " SECURETOOLS from IOP.JSAHAZARDSDROPPED where PERMITNUMBER =
		// :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsDroppedDto jsaHazardsDroppedDto = new
		// JsaHazardsDroppedDto();
		// for (Object[] a : obj) {
		// jsaHazardsDroppedDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsDroppedDto.setDroppedObjects(Integer.parseInt(a[1].toString()));
		// jsaHazardsDroppedDto.setMarkRestrictEntry(Integer.parseInt(a[2].toString()));
		// jsaHazardsDroppedDto.setUseLiftingEquipmentToRaise(Integer.parseInt(a[3].toString()));
		// jsaHazardsDroppedDto.setSecureTools(Integer.parseInt(a[4].toString()));
		// }
		// logger.info(jsaHazardsDroppedDto.toString());
		// payload.setJsaHazardsDroppedDto(jsaHazardsDroppedDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsDroppedDto(jsaHazardsDroppedDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// LIFTINGEQUIPMENT,CONFIRMEQUIPMENTCONDITION, "
		// + " OBTAINAPPROVALFORLIFTS,HAVEDOCUMENTEDLIFTPLAN from
		// IOP.JSAHAZARDSLIFTING "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsLiftingDto jsaHazardsLiftingDto = new
		// JsaHazardsLiftingDto();
		// for (Object[] a : obj) {
		// jsaHazardsLiftingDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsLiftingDto.setLiftingEquipment(Integer.parseInt(a[1].toString()));
		// jsaHazardsLiftingDto.setConfirmEquipmentCondition(Integer.parseInt(a[2].toString()));
		// jsaHazardsLiftingDto.setObtainApprovalForLifts(Integer.parseInt(a[3].toString()));
		// jsaHazardsLiftingDto.setHaveDocumentedLiftPlan(Integer.parseInt(a[4].toString()));
		// }
		// logger.info(jsaHazardsLiftingDto.toString());
		// payload.setJsaHazardsLiftingDto(jsaHazardsLiftingDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsLiftingDto(jsaHazardsLiftingDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// WORKATHEIGHTS,DISCUSSWORKINGPRACTICE,VERIFYFALLRESTRAINT, "
		// + " USEFULLBODYHARNESS,USELOCKTYPESNAPHOOOKS from
		// IOP.JSAHAZARDSHEIGHTS "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsHeightsDto jsaHazardsHeightsDto = new
		// JsaHazardsHeightsDto();
		// for (Object[] a : obj) {
		// jsaHazardsHeightsDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsHeightsDto.setWorkAtHeights(Integer.parseInt(a[1].toString()));
		// jsaHazardsHeightsDto.setDiscussWorkingPractice(Integer.parseInt(a[2].toString()));
		// jsaHazardsHeightsDto.setVerifyFallRestraint(Integer.parseInt(a[3].toString()));
		// jsaHazardsHeightsDto.setUseFullBodyHarness(Integer.parseInt(a[4].toString()));
		// jsaHazardsHeightsDto.setUseLockTypeSnaphoooks(Integer.parseInt(a[5].toString()));
		// }
		// logger.info(jsaHazardsHeightsDto.toString());
		// payload.setJsaHazardsHeightsDto(jsaHazardsHeightsDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsHeightsDto(jsaHazardsHeightsDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// PORTABLEELECTRICALEQUIPMENT,INSPECTTOOLSFORCONDITION, "
		// + "
		// IMPLEMENTGASTESTING,PROTECTELECTRICALLEADS,IDENTIFYEQUIPCLASSIFICATION
		// "
		// + " from IOP.JSAHAZARDSELECTRICAL where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsElectricalDto jsaHazardsElectricalDto = new
		// JsaHazardsElectricalDto();
		// for (Object[] a : obj) {
		// jsaHazardsElectricalDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsElectricalDto.setPortableElectricalEquipment(Integer.parseInt(a[1].toString()));
		// jsaHazardsElectricalDto.setInspectToolsForCondition(Integer.parseInt(a[2].toString()));
		// jsaHazardsElectricalDto.setImplementGasTesting(Integer.parseInt(a[3].toString()));
		// jsaHazardsElectricalDto.setProtectElectricalLeads(Integer.parseInt(a[4].toString()));
		// jsaHazardsElectricalDto.setIdentifyEquipClassification(Integer.parseInt(a[5].toString()));
		// }
		// logger.info(jsaHazardsElectricalDto.toString());
		// payload.setJsaHazardsElectricalDto(jsaHazardsElectricalDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsElectricalDto(jsaHazardsElectricalDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// MOVINGEQUIPMENT,CONFIRMMACHINERYINTEGRITY, "
		// + "
		// PROVIDEPROTECTIVEBARRIERS,OBSERVERTOMONITORPROXIMITYPEOPLEANDEQUIPMENT,LOCKOUTEQUIPMENT,
		// "
		// + " DONOTWORKINLINEOFFIRE from IOP.JSAHAZARDSMOVING where
		// PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsMovingDto jsaHazardsMovingDto = new JsaHazardsMovingDto();
		// for (Object[] a : obj) {
		// jsaHazardsMovingDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsMovingDto.setMovingEquipment(Integer.parseInt(a[1].toString()));
		// jsaHazardsMovingDto.setConfirmMachineryIntegrity(Integer.parseInt(a[2].toString()));
		// jsaHazardsMovingDto.setProvideProtectiveBarriers(Integer.parseInt(a[3].toString()));
		// jsaHazardsMovingDto.setObserverToMonitorProximityPeopleAndEquipment(Integer.parseInt(a[4].toString()));
		// jsaHazardsMovingDto.setLockOutEquipment(Integer.parseInt(a[5].toString()));
		// jsaHazardsMovingDto.setDoNotWorkInLineOfFire(Integer.parseInt(a[6].toString()));
		// }
		// logger.info(jsaHazardsMovingDto.toString());
		// payload.setJsaHazardsMovingDto(jsaHazardsMovingDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsMovingDto(jsaHazardsMovingDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// MANUALHANDLING,ASSESSMANUALTASK,LIMITLOADSIZE, "
		// + " PROPERLIFTINGTECHNIQUE,CONFIRMSTABILITYOFLOAD,GETASSISTANCEORAID
		// "
		// + " from IOP.JSAHAZARDSMANUAL where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsManualDto jsaHazardsManualDto = new JsaHazardsManualDto();
		// for (Object[] a : obj) {
		// jsaHazardsManualDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsManualDto.setManualHandling(Integer.parseInt(a[1].toString()));
		// jsaHazardsManualDto.setAssessManualTask(Integer.parseInt(a[2].toString()));
		// jsaHazardsManualDto.setLimitLoadSize(Integer.parseInt(a[3].toString()));
		// jsaHazardsManualDto.setProperLiftingTechnique(Integer.parseInt(a[4].toString()));
		// jsaHazardsManualDto.setConfirmStabilityOfLoad(Integer.parseInt(a[5].toString()));
		// jsaHazardsManualDto.setGetAssistanceOrAid(Integer.parseInt(a[6].toString()));
		//
		// }
		// logger.info(jsaHazardsManualDto.toString());
		// payload.setJsaHazardsManualDto(jsaHazardsManualDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsManualDto(jsaHazardsManualDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// EQUIPMENTANDTOOLS,INSPECTEQUIPMENTTOOL, "
		// + "
		// BRASSTOOLSNECESSARY,USEPROTECTIVEGUARDS,USECORRECTTOOLS,CHECKFORSHARPEDGES,
		// "
		// + " APPLYHANDSAFETYPRINCIPLE from IOP.JSAHAZARDSTOOLS where
		// PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsToolsDto jsaHazardsToolsDto = new JsaHazardsToolsDto();
		// for (Object[] a : obj) {
		// jsaHazardsToolsDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsToolsDto.setEquipmentAndTools(Integer.parseInt(a[1].toString()));
		// jsaHazardsToolsDto.setInspectEquipmentTool(Integer.parseInt(a[2].toString()));
		// jsaHazardsToolsDto.setBrassToolsNecessary(Integer.parseInt(a[3].toString()));
		// jsaHazardsToolsDto.setUseProtectiveGuards(Integer.parseInt(a[4].toString()));
		// jsaHazardsToolsDto.setUseCorrectTools(Integer.parseInt(a[5].toString()));
		// jsaHazardsToolsDto.setCheckForSharpEdges(Integer.parseInt(a[6].toString()));
		// jsaHazardsToolsDto.setApplyHandSafetyPrinciple(Integer.parseInt(a[7].toString()));
		// }
		// logger.info(jsaHazardsToolsDto.toString());
		// payload.setJsaHazardsToolsDto(jsaHazardsToolsDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsToolsDto(jsaHazardsToolsDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// SLIPSTRIPSANDFALLS,IDENTIFYPROJECTIONS,FLAGHAZARDS, "
		// + " SECURECABLES,CLEANUPLIQUIDS,BARRICADEHOLES from
		// IOP.JSAHAZARDSFALLS "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsFallsDto jsaHazardsFallsDto = new JsaHazardsFallsDto();
		// for (Object[] a : obj) {
		// jsaHazardsFallsDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsFallsDto.setSlipsTripsAndFalls(Integer.parseInt(a[1].toString()));
		// jsaHazardsFallsDto.setIdentifyProjections(Integer.parseInt(a[2].toString()));
		// jsaHazardsFallsDto.setFlagHazards(Integer.parseInt(a[3].toString()));
		// jsaHazardsFallsDto.setSecureCables(Integer.parseInt(a[4].toString()));
		// jsaHazardsFallsDto.setCleanUpLiquids(Integer.parseInt(a[5].toString()));
		// jsaHazardsFallsDto.setBarricadeHoles(Integer.parseInt(a[6].toString()));
		//
		// }
		// logger.info(jsaHazardsFallsDto.toString());
		// payload.setJsaHazardsFallsDto(jsaHazardsFallsDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsFallsDto(jsaHazardsFallsDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// HIGHVOLTAGE,RESTRICTACCESS,DISCHARGEEQUIPMENT, "
		// + " OBSERVESAFEWORKDISTANCE,USEFLASHBURN,USEINSULATEDGLOVES from
		// IOP.JSAHAZARDSVOLTAGE "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsVoltageDto jsaHazardsVoltageDto = new
		// JsaHazardsVoltageDto();
		// for (Object[] a : obj) {
		// jsaHazardsVoltageDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsVoltageDto.setHighVoltage(Integer.parseInt(a[1].toString()));
		// jsaHazardsVoltageDto.setRestrictAccess(Integer.parseInt(a[2].toString()));
		// jsaHazardsVoltageDto.setDischargeEquipment(Integer.parseInt(a[3].toString()));
		// jsaHazardsVoltageDto.setObserveSafeWorkDistance(Integer.parseInt(a[4].toString()));
		// jsaHazardsVoltageDto.setUseFlashBurn(Integer.parseInt(a[5].toString()));
		// jsaHazardsVoltageDto.setUseInsulatedGloves(Integer.parseInt(a[6].toString()));
		// }
		// logger.info(jsaHazardsVoltageDto.toString());
		// payload.setJsaHazardsVoltageDto(jsaHazardsVoltageDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsVoltageDto(jsaHazardsVoltageDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// EXCAVATIONS,HAVEEXCAVATIONPLAN,LOCATEPIPESBYHANDDIGGING, "
		// + " DEENERGIZEUNDERGROUND,CSECONTROLS from IOP.JSAHAZARDSEXCAVATION "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsExcavationdDto jsaHazardsExcavationdDto = new
		// JsaHazardsExcavationdDto();
		// for (Object[] a : obj) {
		// jsaHazardsExcavationdDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsExcavationdDto.setExcavations(Integer.parseInt(a[1].toString()));
		// jsaHazardsExcavationdDto.setHaveExcavationPlan(Integer.parseInt(a[2].toString()));
		// jsaHazardsExcavationdDto.setLocatePipesByHandDigging(Integer.parseInt(a[3].toString()));
		// jsaHazardsExcavationdDto.setDeEnergizeUnderground(Integer.parseInt(a[4].toString()));
		// jsaHazardsExcavationdDto.setCseControls(Integer.parseInt(a[5].toString()));
		// }
		// logger.info(jsaHazardsExcavationdDto.toString());
		// payload.setJsaHazardsExcavationdDto(jsaHazardsExcavationdDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsExcavationdDto(jsaHazardsExcavationdDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select distinct PERMITNUMBER,
		// MOBILEEQUIPMENT,ASSESSEQUIPMENTCONDITION,CONTROLACCESS, "
		// + " MONITORPROXIMITY,MANAGEOVERHEADHAZARDS,ADHERETORULES from
		// IOP.JSAHAZARDSMOBILE "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// JsaHazardsMobileDto jsaHazardsMobileDto = new JsaHazardsMobileDto();
		// for (Object[] a : obj) {
		// jsaHazardsMobileDto.setPermitNumber((Integer) a[0]);
		// jsaHazardsMobileDto.setMobileEquipment(Integer.parseInt(a[1].toString()));
		// jsaHazardsMobileDto.setAssessEquipmentCondition(Integer.parseInt(a[2].toString()));
		// jsaHazardsMobileDto.setControlAccess(Integer.parseInt(a[3].toString()));
		// jsaHazardsMobileDto.setMonitorProximity(Integer.parseInt(a[4].toString()));
		// jsaHazardsMobileDto.setManageOverheadHazards(Integer.parseInt(a[5].toString()));
		// jsaHazardsMobileDto.setAdhereToRules(Integer.parseInt(a[6].toString()));
		// }
		// logger.info(jsaHazardsMobileDto.toString());
		// payload.setJsaHazardsMobileDto(jsaHazardsMobileDto);
		// getJsaByPermitNumPayloadDto.setJsaHazardsMobileDto(jsaHazardsMobileDto);
		// //////////////////////////////////////////////////////////////
		// sql = "select SERIALNO,PERMITNUMBER,
		// TASKSTEPS,POTENTIALHAZARDS,HAZARDCONTROLS,PERSONRESPONSIBLE "
		// + " from IOP.JSASTEPS where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		//
		// List<JsaStepsDto> jsaStepsDtoList = new ArrayList<JsaStepsDto>();
		// for (Object[] a : obj) {
		// JsaStepsDto jsaStepsDto = new JsaStepsDto();
		// jsaStepsDto.setSerialNo((Integer) a[0]);
		// jsaStepsDto.setPermitNumber((Integer) a[1]);
		// jsaStepsDto.setTaskSteps((String) a[2]);
		// jsaStepsDto.setPotentialHazards((String) a[3]);
		// jsaStepsDto.setHazardControls((String) a[4]);
		// jsaStepsDto.setPersonResponsible((String) a[5]);
		// jsaStepsDtoList.add(jsaStepsDto);
		// }
		// logger.info(jsaStepsDtoList.toString());
		// payload.setJsaStepsDtoList(jsaStepsDtoList);
		// getJsaByPermitNumPayloadDto.setJsaStepsDtoList(jsaStepsDtoList);
		// //////////////////////////////////////////////////////////////
		// sql = "select SERIALNO,PERMITNUMBER, LINEDESCRIPTION from
		// IOP.JSASTOPTRIGGER "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// List<JsaStopTriggerDto> jsaStopTriggerDtoList = new
		// ArrayList<JsaStopTriggerDto>();
		// for (Object[] a : obj) {
		// JsaStopTriggerDto jsaStopTriggerDto = new JsaStopTriggerDto();
		// jsaStopTriggerDto.setSerialNo((Integer) a[0]);
		// jsaStopTriggerDto.setPermitNumber((Integer) a[1]);
		// jsaStopTriggerDto.setLineDescription((String) a[2]);
		// jsaStopTriggerDtoList.add(jsaStopTriggerDto);
		// }
		// logger.info(jsaStopTriggerDtoList.toString());
		// payload.setJsaStopTriggerDtoList(jsaStopTriggerDtoList);
		// getJsaByPermitNumPayloadDto.setJsaStopTriggerDtoList(jsaStopTriggerDtoList);
		// //////////////////////////////////////////////////////////////
		// sql = "select SERIALNO,PERMITNUMBER,
		// FACILTYORSITE,HIERARCHYLEVEL,FACILITY,MUWI from IOP.JSA_LOCATION "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// List<JsaLocationDto> jsaLocationDtoList = new
		// ArrayList<JsaLocationDto>();
		// for (Object[] a : obj) {
		// JsaLocationDto jsaLocationDto = new JsaLocationDto();
		// jsaLocationDto.setSerialNo((Integer) a[0]);
		// jsaLocationDto.setPermitNumber((Integer) a[1]);
		// jsaLocationDto.setFacilityOrSite((String) a[2]);
		// jsaLocationDto.setHierachyLevel((String) a[3]);
		// jsaLocationDto.setFacility((String) a[4]);
		// jsaLocationDto.setMuwi((String) a[5]);
		// jsaLocationDtoList.add(jsaLocationDto);
		// }
		// logger.info(jsaLocationDtoList.toString());
		// payload.setJsaLocationDtoList(jsaLocationDtoList);
		// getJsaByPermitNumPayloadDto.setJsaLocationDtoList(jsaLocationDtoList);
		// //////////////////////////////////////////////////////////////
		// sql = "select SERIALNO,PERMITNUMBER,
		// FIRSTNAME,LASTTNAME,CONTACTNUMBER,HASSIGNEDJSA, "
		// + " HASSIGNEDCWP,HASSIGNEDHWP,HASSIGNEDCSE from IOP.PTWPEOPLE "
		// + " where PERMITNUMBER = :permitNum";
		// q = getSession().createNativeQuery(sql);
		// q.setParameter("permitNum", permitNum);
		// obj = q.getResultList();
		// List<PtwPeopleDto> ptwPeopleDtoList = new ArrayList<PtwPeopleDto>();
		// for (Object[] a : obj) {
		// PtwPeopleDto ptwPeopleDto = new PtwPeopleDto();
		// ptwPeopleDto.setSerialNo((Integer) a[0]);
		// ptwPeopleDto.setPermitNumber((Integer) a[1]);
		// ptwPeopleDto.setFirstName((String) a[2]);
		// ptwPeopleDto.setLastName((String) a[3]);
		// ptwPeopleDto.setContactNumber((String) a[4]);
		// ptwPeopleDto.setHasSignedJSA(Integer.parseInt(a[5].toString()));
		// ptwPeopleDto.setHasSignedCWP(Integer.parseInt(a[6].toString()));
		// ptwPeopleDto.setHasSignedHWP(Integer.parseInt(a[7].toString()));
		// ptwPeopleDto.setHasSignedCSE(Integer.parseInt(a[8].toString()));
		// ptwPeopleDtoList.add(ptwPeopleDto);
		// }
		// logger.info(ptwPeopleDtoList.toString());
		// payload.setPtwPeopleDtoList(ptwPeopleDtoList);
		// getJsaByPermitNumPayloadDto.setPtwPeopleDtoList(ptwPeopleDtoList);
		// //////////////////////////////////////////////////////////////
		// logger.info("GetJsaByPermitNumDao | Final Output" +
		// getJsaByPermitNumPayloadDto);
		// return getJsaByPermitNumPayloadDto;
		// } catch (Exception e) {
		// logger.error("GetJsaByPermitNumDao | getJsaByPermitNum error" +
		// e.getMessage());
		// }
		// return null;

	}

}
