package com.incture.ptw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.GetJsaByPermitNumPayloadDto;
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
import com.incture.ptw.dto.JsaHazardsPersonnelDto;
import com.incture.ptw.dto.JsaHazardsPressurizedDto;
import com.incture.ptw.dto.JsaHazardsSimultaneousDto;
import com.incture.ptw.dto.JsaHazardsSpillsDto;
import com.incture.ptw.dto.JsaHazardsSubstancesDto;
import com.incture.ptw.dto.JsaHazardsToolsDto;
import com.incture.ptw.dto.JsaHazardsVisibilityDto;
import com.incture.ptw.dto.JsaHazardsVoltageDto;
import com.incture.ptw.dto.JsaHazardsWeatherDto;
import com.incture.ptw.dto.JsaHazardscseDto;
import com.incture.ptw.dto.JsaLocationDto;
import com.incture.ptw.dto.JsaReviewDto;
import com.incture.ptw.dto.JsaRiskAssesmentDto;
import com.incture.ptw.dto.JsaStepsDto;
import com.incture.ptw.dto.JsaStopTriggerDto;
import com.incture.ptw.dto.JsaheaderDto;
import com.incture.ptw.dto.JsappeDto;
import com.incture.ptw.dto.PtwPeopleDto;

@Repository("GetJsaByPermitNumDao")
public class GetJsaByPermitNumDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public GetJsaByPermitNumPayloadDto getJsaByPermitNum(String permitNumber) {
		GetJsaByPermitNumPayloadDto getJsaByPermitNumPayloadDto = new GetJsaByPermitNumPayloadDto();
		List<Object[]> obj;
		try {
			String sql = "select distinct PERMITNUMBER, JSAPERMITNUMBER,HASCWP,HASHWP, "
					+ " HASCSE,TASKDESCRIPTION,IDENTIFYMOSTSERIOUSPOTENTIALINJURY,ISACTIVE,STATUS from "
					+ " IOP.JSAHEADER where PERMITNUMBER = :permitNumber";
			logger.info("JSAHEADER sql " + sql);
			Query q = getSession().createNativeQuery(sql);
			q.setParameter("permitNumber", permitNumber);
			obj = q.getResultList();
			JsaheaderDto jsaheaderDto = new JsaheaderDto();
			for (Object[] a : obj) {
				jsaheaderDto.setPermitNumber((Integer) a[0]);
				jsaheaderDto.setJsaPermitNumber((String) a[1]);
				jsaheaderDto.setHasCwp(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasHwp(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setHasCse(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setTaskDescription((String) a[5]);
				jsaheaderDto.setIdentifyMostSeriousPotentialInjury((String) a[6]);
				jsaheaderDto.setIsActive(Integer.parseInt(a[2].toString()));
				jsaheaderDto.setStatus((String) a[8]);
			}

			logger.info(jsaheaderDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHEADER(jsaheaderDto);
			////////////////////////////////////////////////////////////

			Integer permitNum = jsaheaderDto.getPermitNumber(); // as per xsjs
																// logic and
																// this permit
																// number is
																// used
																// everywhere
			sql = "select distinct PERMITNUMBER, CREATEDBY,CREATEDDATE,APPROVEDBY,APPROVEDDATE,LASTUPDATEDBY, "
					+ " LASTUPDATEDDATE from IOP.JSAREVIEW where PERMITNUMBER = :permitNum";
			logger.info("JSAREVIEW sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaReviewDto jsaReviewDto = new JsaReviewDto();
			for (Object[] a : obj) {
				jsaReviewDto.setPermitNumber((Integer) a[0]);
				jsaReviewDto.setCreatedBy((String) a[1]);
				jsaReviewDto.setCreatedDate((Date) a[2]);
				jsaReviewDto.setApprovedBy((String) a[3]);
				jsaReviewDto.setApprovedDate((Date) a[4]);
				jsaReviewDto.setLastUpdatedBy((String) a[5]);
				jsaReviewDto.setLastUpdatedDate((Date) a[6]);
			}
			logger.info(jsaReviewDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAREVIEW(jsaReviewDto);
			///////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, MUSTMODIFYEXISTINGWORKPRACTICE,HASCONTINUEDRISK "
					+ " from IOP.JSARISKASSESMENT where PERMITNUMBER = :permitNum";
			logger.info("JSARISKASSESMENT sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaRiskAssesmentDto jsaRiskAssesmentDto = new JsaRiskAssesmentDto();
			for (Object[] a : obj) {
				jsaRiskAssesmentDto.setPermitNumber((Integer) a[0]);
				jsaRiskAssesmentDto.setMustModifyExistingWorkPractice(Integer.parseInt(a[1].toString()));
				jsaRiskAssesmentDto.setHasContinuedRisk(Integer.parseInt(a[2].toString()));
			}
			logger.info(jsaRiskAssesmentDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSARISKASS(jsaRiskAssesmentDto);
			/////////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, HARDHAT,SAFETYBOOT,GOGGLES,FACESHIELD,SAFETYGLASSES, "
					+ " SINGLEEAR,DOUBLEEARS,RESPIRATORTYPEDESCRIPTION,NEEDSCBA,NEEDDUSTMASK,COTTONGLOVE, "
					+ " LEATHERGLOVE,IMPACTPROTECTION,GLOVEDESCRIPTION,CHEMICALGLOVEDESCRIPTION,FALLPROTECTION, "
					+ " FALLRESTRAINT,CHEMICALSUIT,APRON,FLAMERESISTANTCLOTHING,OTHERPPEDESCRIPTION, "
					+ " NEEDFOULWEATHERGEAR,HAVECONSENTOFTASKLEADER,COMPANYOFTASKLEADER from IOP.JSA_PPE "
					+ " where PERMITNUMBER = :permitNum";
			logger.info("JSA_PPE sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsappeDto jsappeDto = new JsappeDto();
			for (Object[] a : obj) {
				jsappeDto.setPermitNumber((Integer) a[0]);
				jsappeDto.setHardHat(Integer.parseInt(a[1].toString()));
				jsappeDto.setSafetyBoot(Integer.parseInt(a[2].toString()));
				jsappeDto.setGoggles(Integer.parseInt(a[3].toString()));
				jsappeDto.setFaceShield(Integer.parseInt(a[4].toString()));
				jsappeDto.setSafetyGlasses(Integer.parseInt(a[5].toString()));
				jsappeDto.setSingleEar(Integer.parseInt(a[6].toString()));
				jsappeDto.setDoubleEars(Integer.parseInt(a[7].toString()));
				jsappeDto.setRespiratorTypeDescription((String) a[8]);
				jsappeDto.setNeedScba(Integer.parseInt(a[9].toString()));
				jsappeDto.setNeedDustMask(Integer.parseInt(a[10].toString()));
				jsappeDto.setCottonGlove(Integer.parseInt(a[11].toString()));
				jsappeDto.setLeatherGlove(Integer.parseInt(a[12].toString()));
				jsappeDto.setImpactProtection(Integer.parseInt(a[13].toString()));
				jsappeDto.setGloveDescription((String) a[14]);
				jsappeDto.setChemicalGloveDescription((String) a[15]);
				jsappeDto.setFallProtection(Integer.parseInt(a[16].toString()));
				jsappeDto.setFallRestraint(Integer.parseInt(a[17].toString()));
				jsappeDto.setChemicalSuit(Integer.parseInt(a[18].toString()));
				jsappeDto.setApron(Integer.parseInt(a[19].toString()));
				jsappeDto.setFlameResistantClothing(Integer.parseInt(a[20].toString()));
				jsappeDto.setOtherppeDescription((String) a[21]);
				jsappeDto.setNeedFoulWeatherGear((String) a[22]); // need to
																	// convert
																	// this into
																	// string as
																	// per xsjs
																	// payload
				jsappeDto.setHaveConsentOfTaskLeader(Integer.parseInt(a[23].toString()));
				jsappeDto.setCompanyOfTaskLeader((String) a[24]);
			}
			logger.info(jsappeDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSE_PPE(jsappeDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, PRESURIZEDEQUIPMENT,PERFORMISOLATION,DEPRESSURIZEDRAIN, "
					+ " RELIEVETRAPPEDPRESSURE,DONOTWORKINLINEOFFIRE,ANTICIPATERESIDUAL,SECUREALLHOSES "
					+ " from IOP.JSAHAZARDSPRESSURIZED where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPRESSURIZED sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsPressurizedDto jsaHazardsPressurizedDto = new JsaHazardsPressurizedDto();
			for (Object[] a : obj) {
				jsaHazardsPressurizedDto.setPermitNumber((Integer) a[0]);
				jsaHazardsPressurizedDto.setPresurizedEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsPressurizedDto.setPerformIsolation(Integer.parseInt(a[2].toString()));
				jsaHazardsPressurizedDto.setDepressurizeDrain(Integer.parseInt(a[3].toString()));
				jsaHazardsPressurizedDto.setRelieveTrappedPressure(Integer.parseInt(a[4].toString()));
				jsaHazardsPressurizedDto.setDoNotWorkInlineOfFire(Integer.parseInt(a[5].toString()));
				jsaHazardsPressurizedDto.setAnticipateResidual(Integer.parseInt(a[6].toString()));
				jsaHazardsPressurizedDto.setSecureAllHoses(Integer.parseInt(a[7].toString()));
			}
			logger.info(jsaHazardsPressurizedDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDPRESS(jsaHazardsPressurizedDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, POORLIGHTING,PROVIDEALTERNATELIGHTING, "
					+ " WAITUNTILVISIBILITYIMPROVE,DEFERUNTILVISIBILITYIMPROVE,KNOWDISTANCEFROMPOLES "
					+ " from IOP.JSAHAZARDSVISIBILITY where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSVISIBILITY sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsVisibilityDto jsaHazardsVisibilityDto = new JsaHazardsVisibilityDto();
			for (Object[] a : obj) {
				jsaHazardsVisibilityDto.setPermitNumber((Integer) a[0]);
				jsaHazardsVisibilityDto.setPoorLighting(Integer.parseInt(a[1].toString()));
				jsaHazardsVisibilityDto.setAlternateLighting(Integer.parseInt(a[2].toString()));
				jsaHazardsVisibilityDto.setWaitUntilVisibilityImprove(Integer.parseInt(a[3].toString()));
				jsaHazardsVisibilityDto.setDeferUntilVisibility(Integer.parseInt(a[4].toString()));
				jsaHazardsVisibilityDto.setKnowDistanceFromPoles(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsVisibilityDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDVISIBLE(jsaHazardsVisibilityDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, PERSONNEL,PERFORMINDUCTION,MENTORCOACHSUPERVISE, "
					+ " VERIFYCOMPETENCIES,ADDRESSLIMITATIONS,MANAGELANGUAGEBARRIERS,WEARSEATBELTS "
					+ " from IOP.JSAHAZARDSPERSONNEL where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPERSONNEL sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsPersonnelDto jsaHazardsPersonnelDto = new JsaHazardsPersonnelDto();
			for (Object[] a : obj) {
				jsaHazardsPersonnelDto.setPermitNumber((Integer) a[0]);
				jsaHazardsPersonnelDto.setPersonnel(Integer.parseInt(a[1].toString()));
				jsaHazardsPersonnelDto.setPerformInduction(Integer.parseInt(a[2].toString()));
				jsaHazardsPersonnelDto.setMentorCoachSupervise(Integer.parseInt(a[3].toString()));
				jsaHazardsPersonnelDto.setVerifyCompetencies(Integer.parseInt(a[4].toString()));
				jsaHazardsPersonnelDto.setAddressLimitations(Integer.parseInt(a[5].toString()));
				jsaHazardsPersonnelDto.setManageLanguageBarriers(Integer.parseInt(a[6].toString()));
				jsaHazardsPersonnelDto.setWearSeatBelts(Integer.parseInt(a[7].toString()));
			}
			logger.info(jsaHazardsPersonnelDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDPERSON(jsaHazardsPersonnelDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, CONFINEDSPACEENTRY,DISCUSSWORKPRACTICE, "
					+ " CONDUCTATMOSPHERICTESTING,MONITORACCESS,PROTECTSURFACES,PROHIBITMOBILEENGINE, "
					+ " PROVIDEOBSERVER,DEVELOPRESCUEPLAN from IOP.JSAHAZARDSCSE where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSCSE sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardscseDto jsaHazardscseDto = new JsaHazardscseDto();
			for (Object[] a : obj) {
				jsaHazardscseDto.setPermitNumber((Integer) a[0]);
				jsaHazardscseDto.setConfinedSpaceEntry(Integer.parseInt(a[1].toString()));
				jsaHazardscseDto.setDiscussWorkPractice(Integer.parseInt(a[2].toString()));
				jsaHazardscseDto.setConductAtmosphericTesting(Integer.parseInt(a[3].toString()));
				jsaHazardscseDto.setMonitorAccess(Integer.parseInt(a[4].toString()));
				jsaHazardscseDto.setProtectSurfaces(Integer.parseInt(a[5].toString()));
				jsaHazardscseDto.setProhibitMobileEngine(Integer.parseInt(a[6].toString()));
				jsaHazardscseDto.setProvideObserver(Integer.parseInt(a[7].toString()));
				jsaHazardscseDto.setDevelopRescuePlan(Integer.parseInt(a[8].toString()));
			}
			logger.info(jsaHazardscseDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDCSE(jsaHazardscseDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, SIMULTANEOUSOPERATIONS,FOLLOWSIMOPSMATRIX, "
					+" MOCREQUIREDFOR,INTERFACEBETWEENGROUPS,USEBARRIERSAND,HAVEPERMITSIGNED "
					+" from IOP.JSAHAZARDSSIMULTANEOUS where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSSIMULTANEOUS sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsSimultaneousDto jsaHazardsSimultaneousDto = new JsaHazardsSimultaneousDto();
			for (Object[] a : obj) {
				jsaHazardsSimultaneousDto.setPermitNumber((Integer)a[0]);
				jsaHazardsSimultaneousDto.setSimultaneousOperations(Integer.parseInt(a[1].toString()));
				jsaHazardsSimultaneousDto.setFollowsImopsMatrix(Integer.parseInt(a[2].toString()));
				jsaHazardsSimultaneousDto.setMocRequiredFor(Integer.parseInt(a[3].toString()));
				jsaHazardsSimultaneousDto.setInterfaceBetweenGroups(Integer.parseInt(a[4].toString()));
				jsaHazardsSimultaneousDto.setUseBarriersAnd(Integer.parseInt(a[5].toString()));
				jsaHazardsSimultaneousDto.setHavePermitSigned(Integer.parseInt(a[6].toString()));
			}
			logger.info(jsaHazardsSimultaneousDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDSIMULTAN(jsaHazardsSimultaneousDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER,IGNITIONSOURCES,REMOVECOMBUSTIBLEMATERIALS,PROVIDEFIREWATCH, "
					+" IMPLEMENTABRASIVEBLASTINGCONTROLS,CONDUCTCONTINUOUSGASTESTING,EARTHFORSTATICELECTRICITY "
					+" from IOP.JSAHAZARDSIGNITION where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSIGNITION sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsIgnitionDto jsaHazardsIgnitionDto = new JsaHazardsIgnitionDto();
			for (Object[] a : obj) {
				jsaHazardsIgnitionDto.setPermitNumber((Integer)a[0]);
				jsaHazardsIgnitionDto.setIgnitionSources(Integer.parseInt(a[1].toString()));
				jsaHazardsIgnitionDto.setRemoveCombustibleMaterials(Integer.parseInt(a[2].toString()));
				jsaHazardsIgnitionDto.setProvideFireWatch(Integer.parseInt(a[3].toString()));
				jsaHazardsIgnitionDto.setImplementAbrasiveBlastingControls(Integer.parseInt(a[4].toString()));
				jsaHazardsIgnitionDto.setConductContinuousGasTesting(Integer.parseInt(a[5].toString()));
				jsaHazardsIgnitionDto.setEarthForStaticElectricity(Integer.parseInt(a[6].toString()));
			}
			logger.info(jsaHazardsIgnitionDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDIGNITION(jsaHazardsIgnitionDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, HAZARDOUSSUBSTANCES,DRAINEQUIPMENT,FOLLOWSDSCONTROLS, "
					+" IMPLEMENTHEALTHHAZARDCONTROLS,TESTMATERIAL from IOP.JSAHAZARDSSUBSTANCES "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsSubstancesDto jsaHazardsSubstancesDto = new JsaHazardsSubstancesDto();
			for (Object[] a : obj) {
				jsaHazardsSubstancesDto.setPermitNumber((Integer)a[0]);
				jsaHazardsSubstancesDto.setHazardousSubstances(Integer.parseInt(a[1].toString()));
				jsaHazardsSubstancesDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
				jsaHazardsSubstancesDto.setFollowsDSControls(Integer.parseInt(a[3].toString()));
				jsaHazardsSubstancesDto.setImplementHealthHazardControls(Integer.parseInt(a[4].toString()));
				jsaHazardsSubstancesDto.setTestMaterial(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsSubstancesDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDSUBS(jsaHazardsSubstancesDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, POTENTIALSPILLS,DRAINEQUIPMENT,CONNECTIONSINGOODCONDITION, "
					+" SPILLCONTAINMENTEQUIPMENT,HAVESPILLCLEANUPMATERIALS,RESTRAINHOSESWHENNOTINUSE "
					+" from IOP.JSAHAZARDSSPILLS where PERMITNUMBER = :permitNum";
			
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsSpillsDto jsaHazardsSpillsDto = new JsaHazardsSpillsDto();
			for (Object[] a : obj) {
				jsaHazardsSpillsDto.setPermitNumber((Integer)a[0]);
				jsaHazardsSpillsDto.setPotentialSpills(Integer.parseInt(a[1].toString()));
				jsaHazardsSpillsDto.setDrainEquipment(Integer.parseInt(a[2].toString()));
				jsaHazardsSpillsDto.setConnectionSinGoodCondition(Integer.parseInt(a[3].toString()));
				jsaHazardsSpillsDto.setSpillContainmentEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsSpillsDto.setHaveSpillCleanUpMaterials(Integer.parseInt(a[5].toString()));
				jsaHazardsSpillsDto.setRestRainHosesWhenNotInUse(Integer.parseInt(a[6].toString()));
				
			}
			logger.info(jsaHazardsSpillsDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDSPILL(jsaHazardsSpillsDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, WEATHER,CONTROLSFORSLIPPERYSURFACE,HEATBREAK, "
					+" COLDHEATERS,LIGHTNING from IOP.JSAHAZARDSWEATHER where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsWeatherDto jsaHazardsWeatherDto = new JsaHazardsWeatherDto();
			for (Object[] a : obj) {
				jsaHazardsWeatherDto.setPermitNumber((Integer)a[0]);
				jsaHazardsWeatherDto.setWeather(Integer.parseInt(a[1].toString()));
				jsaHazardsWeatherDto.setControlsForLipperySurface(Integer.parseInt(a[2].toString()));
				jsaHazardsWeatherDto.setHeatBreak(Integer.parseInt(a[3].toString()));
				jsaHazardsWeatherDto.setColdHeaters(Integer.parseInt(a[4].toString()));
				jsaHazardsWeatherDto.setLightning(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsWeatherDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDWEATHER(jsaHazardsWeatherDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, HIGHNOISE,WEARCORRECTHEARING,MANAGEEXPOSURETIMES, "
					+" SHUTDOWNEQUIPMENT,USEQUIETTOOLS,SOUNDBARRIERS,PROVIDESUITABLECOMMS "
					+" from IOP.JSAHAZARDSHIGHNOISE where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsHighNoiseDto jsaHazardsHighNoiseDto = new JsaHazardsHighNoiseDto();
			for (Object[] a : obj) {
				jsaHazardsHighNoiseDto.setPermitNumber((Integer)a[0]);
				jsaHazardsHighNoiseDto.setHighNoise(Integer.parseInt(a[1].toString()));
				jsaHazardsHighNoiseDto.setWearCorrectHearing(Integer.parseInt(a[2].toString()));
				jsaHazardsHighNoiseDto.setManageExposureTimes(Integer.parseInt(a[3].toString()));
				jsaHazardsHighNoiseDto.setShutDownEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsHighNoiseDto.setUseQuietTools(Integer.parseInt(a[5].toString()));
				jsaHazardsHighNoiseDto.setSoundBarriers(Integer.parseInt(a[6].toString()));
				jsaHazardsHighNoiseDto.setProvideSuitablecomms(Integer.parseInt(a[7].toString()));
			}
			logger.info(jsaHazardsHighNoiseDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDNOISE(jsaHazardsHighNoiseDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, DROPPEDOBJECTS,MARKRESTRICTENTRY,USELIFTINGEQUIPMENTTORAISE, "
					+" SECURETOOLS from IOP.JSAHAZARDSDROPPED where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsDroppedDto jsaHazardsDroppedDto = new JsaHazardsDroppedDto();
			for (Object[] a : obj) {
				jsaHazardsDroppedDto.setPermitNumber((Integer)a[0]);
				jsaHazardsDroppedDto.setDroppedObjects(Integer.parseInt(a[1].toString()));
				jsaHazardsDroppedDto.setMarkRestrictEntry(Integer.parseInt(a[2].toString()));
				jsaHazardsDroppedDto.setUseLiftingEquipmentToRaise(Integer.parseInt(a[3].toString()));
				jsaHazardsDroppedDto.setSecureTools(Integer.parseInt(a[4].toString()));
			}
			logger.info(jsaHazardsDroppedDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDDROPPED(jsaHazardsDroppedDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, LIFTINGEQUIPMENT,CONFIRMEQUIPMENTCONDITION, "
					+" OBTAINAPPROVALFORLIFTS,HAVEDOCUMENTEDLIFTPLAN from IOP.JSAHAZARDSLIFTING "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsLiftingDto jsaHazardsLiftingDto = new JsaHazardsLiftingDto();
			for (Object[] a : obj) {
				jsaHazardsLiftingDto.setPermitNumber((Integer)a[0]);
				jsaHazardsLiftingDto.setLiftingEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsLiftingDto.setConfirmEquipmentCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsLiftingDto.setObtainApprovalForLifts(Integer.parseInt(a[3].toString()));
				jsaHazardsLiftingDto.setHaveDocumentedLiftPlan(Integer.parseInt(a[4].toString()));
			}
			logger.info(jsaHazardsLiftingDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDLIFT(jsaHazardsLiftingDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, WORKATHEIGHTS,DISCUSSWORKINGPRACTICE,VERIFYFALLRESTRAINT, "
					+" USEFULLBODYHARNESS,USELOCKTYPESNAPHOOOKS from IOP.JSAHAZARDSHEIGHTS "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsHeightsDto jsaHazardsHeightsDto = new JsaHazardsHeightsDto();
			for (Object[] a : obj) {
				jsaHazardsHeightsDto.setPermitNumber((Integer)a[0]);
				jsaHazardsHeightsDto.setWorkAtHeights(Integer.parseInt(a[1].toString()));
				jsaHazardsHeightsDto.setDiscussWorkingPractice(Integer.parseInt(a[2].toString()));
				jsaHazardsHeightsDto.setVerifyFallRestraint(Integer.parseInt(a[3].toString()));
				jsaHazardsHeightsDto.setUsefullbodyHarness(Integer.parseInt(a[4].toString()));
				jsaHazardsHeightsDto.setUseLockTypeSnapHooks(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsHeightsDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDHEIGHT(jsaHazardsHeightsDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, PORTABLEELECTRICALEQUIPMENT,INSPECTTOOLSFORCONDITION, "
					+" IMPLEMENTGASTESTING,PROTECTELECTRICALLEADS,IDENTIFYEQUIPCLASSIFICATION "
					+" from IOP.JSAHAZARDSELECTRICAL where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsElectricalDto jsaHazardsElectricalDto = new JsaHazardsElectricalDto();
			for (Object[] a : obj) {
				jsaHazardsElectricalDto.setPermitNumber((Integer)a[0]);
				jsaHazardsElectricalDto.setPortableElectricalEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsElectricalDto.setInspectToolsForCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsElectricalDto.setImplementGasTesting(Integer.parseInt(a[3].toString()));
				jsaHazardsElectricalDto.setProtectElectricalLeads(Integer.parseInt(a[4].toString()));
				jsaHazardsElectricalDto.setIdentifyEquipClassification(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsElectricalDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDELECTRICAL(jsaHazardsElectricalDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, MOVINGEQUIPMENT,CONFIRMMACHINERYINTEGRITY, "
					+" PROVIDEPROTECTIVEBARRIERS,OBSERVERTOMONITORPROXIMITYPEOPLEANDEQUIPMENT,LOCKOUTEQUIPMENT, "
					+" DONOTWORKINLINEOFFIRE from IOP.JSAHAZARDSMOVING where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsMovingDto jsaHazardsMovingDto = new JsaHazardsMovingDto();
			for (Object[] a : obj) {
				jsaHazardsMovingDto.setPermitNumber((Integer)a[0]);
				jsaHazardsMovingDto.setMovingEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMovingDto.setConfirmMachineryIntegrity(Integer.parseInt(a[2].toString()));
				jsaHazardsMovingDto.setProvideProtectiveBarriers(Integer.parseInt(a[3].toString()));
				jsaHazardsMovingDto.setObserverToMonitorProximityPeopleAndEquipment(Integer.parseInt(a[4].toString()));
				jsaHazardsMovingDto.setLockOutEquipment(Integer.parseInt(a[5].toString()));
				jsaHazardsMovingDto.setDonotWorkInLineOFFire(Integer.parseInt(a[6].toString()));
			}
			logger.info(jsaHazardsMovingDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDMOVING(jsaHazardsMovingDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, MANUALHANDLING,ASSESSMANUALTASK,LIMITLOADSIZE, "
					+" PROPERLIFTINGTECHNIQUE,CONFIRMSTABILITYOFLOAD,GETASSISTANCEORAID "
					+" from IOP.JSAHAZARDSMANUAL where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsManualDto jsaHazardsManualDto = new JsaHazardsManualDto();
			for (Object[] a : obj) {
				jsaHazardsManualDto.setPermitNumber((Integer)a[0]);
				jsaHazardsManualDto.setManualHandling(Integer.parseInt(a[1].toString()));
				jsaHazardsManualDto.setAssessManualTask(Integer.parseInt(a[2].toString()));
				jsaHazardsManualDto.setLimitLoadSize(Integer.parseInt(a[3].toString()));
				jsaHazardsManualDto.setProperLiftingTechnique(Integer.parseInt(a[4].toString()));
				jsaHazardsManualDto.setConfirmStabilityOfLoad(Integer.parseInt(a[5].toString()));
				jsaHazardsManualDto.setGetAssistanceOrAid(Integer.parseInt(a[6].toString()));
				
			}
			logger.info(jsaHazardsManualDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDMANUAL(jsaHazardsManualDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, EQUIPMENTANDTOOLS,INSPECTEQUIPMENTTOOL, "
					+" BRASSTOOLSNECESSARY,USEPROTECTIVEGUARDS,USECORRECTTOOLS,CHECKFORSHARPEDGES, "
					+" APPLYHANDSAFETYPRINCIPLE from IOP.JSAHAZARDSTOOLS where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsToolsDto jsaHazardsToolsDto = new JsaHazardsToolsDto();
			for (Object[] a : obj) {
				jsaHazardsToolsDto.setPermitNumber((Integer)a[0]);
				jsaHazardsToolsDto.setEquipmentAndTools(Integer.parseInt(a[1].toString()));
				jsaHazardsToolsDto.setInspectEquipmentTool(Integer.parseInt(a[2].toString()));
				jsaHazardsToolsDto.setBrassToolsNecessary(Integer.parseInt(a[3].toString()));
				jsaHazardsToolsDto.setUseProtectiveGuards(Integer.parseInt(a[4].toString()));
				jsaHazardsToolsDto.setUseCorrectTools(Integer.parseInt(a[5].toString()));
				jsaHazardsToolsDto.setCheckForSharpEdges(Integer.parseInt(a[6].toString()));
				jsaHazardsToolsDto.setApplyHandSafetyPrinciple(Integer.parseInt(a[7].toString()));
			}
			logger.info(jsaHazardsToolsDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDTOOLS(jsaHazardsToolsDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, SLIPSTRIPSANDFALLS,IDENTIFYPROJECTIONS,FLAGHAZARDS, "
					+" SECURECABLES,CLEANUPLIQUIDS,BARRICADEHOLES from IOP.JSAHAZARDSFALLS "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsFallsDto jsaHazardsFallsDto = new JsaHazardsFallsDto();
			for (Object[] a : obj) {
				jsaHazardsFallsDto.setPermitNumber((Integer)a[0]);
				jsaHazardsFallsDto.setSlipstripsAndFalls(Integer.parseInt(a[1].toString()));
				jsaHazardsFallsDto.setIdentifyProjections(Integer.parseInt(a[2].toString()));
				jsaHazardsFallsDto.setFlagHazards(Integer.parseInt(a[3].toString()));
				jsaHazardsFallsDto.setSecureCables(Integer.parseInt(a[4].toString()));
				jsaHazardsFallsDto.setCleanupLiquids(Integer.parseInt(a[5].toString()));
				jsaHazardsFallsDto.setBarricadeHoles(Integer.parseInt(a[6].toString()));
				
			}
			logger.info(jsaHazardsFallsDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDFALLS(jsaHazardsFallsDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, HIGHVOLTAGE,RESTRICTACCESS,DISCHARGEEQUIPMENT, "
					+" OBSERVESAFEWORKDISTANCE,USEFLASHBURN,USEINSULATEDGLOVES from IOP.JSAHAZARDSVOLTAGE "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsVoltageDto jsaHazardsVoltageDto = new JsaHazardsVoltageDto();
			for (Object[] a : obj) {
				jsaHazardsVoltageDto.setPermitNumber((Integer)a[0]);
				jsaHazardsVoltageDto.setHighVoltage(Integer.parseInt(a[1].toString()));
				jsaHazardsVoltageDto.setRestrictAccess(Integer.parseInt(a[2].toString()));
				jsaHazardsVoltageDto.setDischargeEquipment(Integer.parseInt(a[3].toString()));
				jsaHazardsVoltageDto.setObserveSafeWorkDistance(Integer.parseInt(a[4].toString()));
				jsaHazardsVoltageDto.setUseFlashBurn(Integer.parseInt(a[5].toString()));
				jsaHazardsVoltageDto.setUseInsulatedGloves(Integer.parseInt(a[6].toString()));
			}
			logger.info(jsaHazardsVoltageDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDVOLTAGE(jsaHazardsVoltageDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, EXCAVATIONS,HAVEEXCAVATIONPLAN,LOCATEPIPESBYHANDDIGGING, "
					+" DEENERGIZEUNDERGROUND,CSECONTROLS from IOP.JSAHAZARDSEXCAVATION "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsExcavationdDto jsaHazardsExcavationdDto = new JsaHazardsExcavationdDto();
			for (Object[] a : obj) {
				jsaHazardsExcavationdDto.setPermitNumber((Integer)a[0]);
				jsaHazardsExcavationdDto.setExcavations(Integer.parseInt(a[1].toString()));
				jsaHazardsExcavationdDto.setHaveExcavationPlan(Integer.parseInt(a[2].toString()));
				jsaHazardsExcavationdDto.setLocatePipeByHandsDigging(Integer.parseInt(a[3].toString()));
				jsaHazardsExcavationdDto.setDeEnergizeUnderground(Integer.parseInt(a[4].toString()));
				jsaHazardsExcavationdDto.setCsecontrols(Integer.parseInt(a[5].toString()));
			}
			logger.info(jsaHazardsExcavationdDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDEXCAVATION(jsaHazardsExcavationdDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, MOBILEEQUIPMENT,ASSESSEQUIPMENTCONDITION,CONTROLACCESS, "
					+" MONITORPROXIMITY,MANAGEOVERHEADHAZARDS,ADHERETORULES from IOP.JSAHAZARDSMOBILE "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsMobileDto jsaHazardsMobileDto = new JsaHazardsMobileDto();
			for (Object[] a : obj) {
				jsaHazardsMobileDto.setPermitNumber((Integer)a[0]);
				jsaHazardsMobileDto.setMobileEquipment(Integer.parseInt(a[1].toString()));
				jsaHazardsMobileDto.setAssessEquipmentCondition(Integer.parseInt(a[2].toString()));
				jsaHazardsMobileDto.setControlAccess(Integer.parseInt(a[3].toString()));
				jsaHazardsMobileDto.setMonitorProximity(Integer.parseInt(a[4].toString()));
				jsaHazardsMobileDto.setManageOverHeadHazards(Integer.parseInt(a[5].toString()));
				jsaHazardsMobileDto.setAdhereToRules(Integer.parseInt(a[6].toString()));
			}
			logger.info(jsaHazardsMobileDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAHAZARDMOBILE(jsaHazardsMobileDto);
			//////////////////////////////////////////////////////////////
			sql = "select  SERIALNO,PERMITNUMBER, TASKSTEPS,POTENTIALHAZARDS,HAZARDCONTROLS,PERSONRESPONSIBLE "
					+" from IOP.JSASTEPS where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			
			
			List<JsaStepsDto> jsaStepsDtoList = new ArrayList<JsaStepsDto>();
			for (Object[] a : obj) {
				JsaStepsDto jsaStepsDto = new JsaStepsDto();
				jsaStepsDto.setSerialNo((Integer)a[0]);
				jsaStepsDto.setPermitNumber((Integer)a[1]);
				jsaStepsDto.setTaskSteps((String)a[2]);
				jsaStepsDto.setPotentialHazards((String)a[3]);
				jsaStepsDto.setHazardControls((String)a[4]);
				jsaStepsDto.setPersonResponsible((String)a[5]);
				jsaStepsDtoList.add(jsaStepsDto);
			}
			logger.info(jsaStepsDtoList.toString());
			getJsaByPermitNumPayloadDto.setTOJSASTEPS(jsaStepsDtoList);
			//////////////////////////////////////////////////////////////
			sql = "select  SERIALNO,PERMITNUMBER, LINEDESCRIPTION from IOP.JSASTOPTRIGGER "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			List<JsaStopTriggerDto> jsaStopTriggerDtoList = new ArrayList<JsaStopTriggerDto>();
			for (Object[] a : obj) {
				JsaStopTriggerDto jsaStopTriggerDto = new JsaStopTriggerDto();
				jsaStopTriggerDto.setSerialNo((Integer)a[0]);
				jsaStopTriggerDto.setPermitNumber((Integer)a[1]);
				jsaStopTriggerDto.setLineDescription((String)a[2]);
				jsaStopTriggerDtoList.add(jsaStopTriggerDto);
			}
			logger.info(jsaStopTriggerDtoList.toString());
			getJsaByPermitNumPayloadDto.setTOJSASTOP(jsaStopTriggerDtoList);
			//////////////////////////////////////////////////////////////
			sql = "select  SERIALNO,PERMITNUMBER, FACILTYORSITE,HIERARCHYLEVEL,FACILITY,MUWI from IOP.JSA_LOCATION "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			List<JsaLocationDto> jsaLocationDtoList = new ArrayList<JsaLocationDto>();
			for (Object[] a : obj) {
				JsaLocationDto jsaLocationDto = new JsaLocationDto();
				jsaLocationDto.setSerialNo((Integer)a[0]);
				jsaLocationDto.setPermitNumber((Integer)a[1]);
				jsaLocationDto.setFaciltyOrSite((String)a[2]);
				jsaLocationDto.setHierarchyLevel((String)a[3]);
				jsaLocationDto.setFacility((String)a[4]);
				jsaLocationDto.setMuwi((String)a[5]);
				jsaLocationDtoList.add(jsaLocationDto);
			}
			logger.info(jsaLocationDtoList.toString());
			getJsaByPermitNumPayloadDto.setTOJSALOCATION(jsaLocationDtoList);
			//////////////////////////////////////////////////////////////
			sql = "select  SERIALNO,PERMITNUMBER, FIRSTNAME,LASTTNAME,CONTACTNUMBER,HASSIGNEDJSA, "
					+" HASSIGNEDCWP,HASSIGNEDHWP,HASSIGNEDCSE from IOP.PTWPEOPLE "
					+" where PERMITNUMBER = :permitNum";
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			List<PtwPeopleDto> ptwPeopleDtoList = new ArrayList<PtwPeopleDto>();
			for (Object[] a : obj) {
				PtwPeopleDto ptwPeopleDto = new PtwPeopleDto();
				ptwPeopleDto.setSerialNo((Integer)a[0]);
				ptwPeopleDto.setPermitNumber((Integer)a[1]);
				ptwPeopleDto.setFirstName((String)a[2]);
				ptwPeopleDto.setLastName((String)a[3]);
				ptwPeopleDto.setContactNumber((String)a[4]);
				ptwPeopleDto.setHasSignedJsa(Integer.parseInt(a[5].toString()));
				ptwPeopleDto.setHasSignedCwp(Integer.parseInt(a[6].toString()));
				ptwPeopleDto.setHasSignedHwp(Integer.parseInt(a[7].toString()));
				ptwPeopleDto.setHasSignedCse(Integer.parseInt(a[8].toString()));
				ptwPeopleDtoList.add(ptwPeopleDto);
			}
			logger.info(ptwPeopleDtoList.toString());
			getJsaByPermitNumPayloadDto.setTOPTWPEOPLE(ptwPeopleDtoList);
			//////////////////////////////////////////////////////////////
			logger.info("GetJsaByPermitNumDao | Final Output" + getJsaByPermitNumPayloadDto);
			return getJsaByPermitNumPayloadDto;
		} catch (Exception e) {
			logger.error("GetJsaByPermitNumDao | getJsaByPermitNum  error" + e.getMessage());
		}
		return null;

	}

}
