package com.incture.ptw.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incture.ptw.dto.GetJsaByPermitNumPayloadDto;
import com.incture.ptw.dto.JsaHazardsPersonnelDto;
import com.incture.ptw.dto.JsaHazardsPressurizedDto;
import com.incture.ptw.dto.JsaHazardsVisibilityDto;
import com.incture.ptw.dto.JsaReviewDto;
import com.incture.ptw.dto.JsaRiskAssesmentDto;
import com.incture.ptw.dto.JsaheaderDto;
import com.incture.ptw.dto.JsappeDto;

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
			
			Integer permitNum = jsaheaderDto.getPermitNumber(); // as per xsjs logic and this permit number is used everywhere
			sql = "select distinct PERMITNUMBER, CREATEDBY,CREATEDDATE,APPROVEDBY,APPROVEDDATE,LASTUPDATEDBY, "
					+ " LASTUPDATEDDATE from IOP.JSAREVIEW where PERMITNUMBER = :permitNum";
			logger.info("JSAREVIEW sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaReviewDto jsaReviewDto = new JsaReviewDto();
			for(Object[] a : obj){
				jsaReviewDto.setPermitNumber((Integer)a[0]);
				jsaReviewDto.setCreatedBy((String)a[1]);
				jsaReviewDto.setCreatedDate((Date)a[2]);
				jsaReviewDto.setApprovedBy((String)a[3]);
				jsaReviewDto.setApprovedDate((Date)a[4]);
				jsaReviewDto.setLastUpdatedBy((String)a[5]);
				jsaReviewDto.setLastUpdatedDate((Date)a[6]);
			}
			logger.info(jsaReviewDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSAREVIEW(jsaReviewDto);
			///////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, MUSTMODIFYEXISTINGWORKPRACTICE,HASCONTINUEDRISK "
					+" from IOP.JSARISKASSESMENT where PERMITNUMBER = :permitNum";
			logger.info("JSARISKASSESMENT sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaRiskAssesmentDto jsaRiskAssesmentDto = new JsaRiskAssesmentDto();
			for(Object[] a : obj){
				jsaRiskAssesmentDto.setPermitNumber((Integer)a[0]);
				jsaRiskAssesmentDto.setMustModifyExistingWorkPractice(Integer.parseInt(a[1].toString()));
				jsaRiskAssesmentDto.setHasContinuedRisk(Integer.parseInt(a[2].toString()));
			}
			logger.info(jsaRiskAssesmentDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSARISKASS(jsaRiskAssesmentDto);
			/////////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, HARDHAT,SAFETYBOOT,GOGGLES,FACESHIELD,SAFETYGLASSES, "
					+" SINGLEEAR,DOUBLEEARS,RESPIRATORTYPEDESCRIPTION,NEEDSCBA,NEEDDUSTMASK,COTTONGLOVE, "
					+" LEATHERGLOVE,IMPACTPROTECTION,GLOVEDESCRIPTION,CHEMICALGLOVEDESCRIPTION,FALLPROTECTION, "
					+" FALLRESTRAINT,CHEMICALSUIT,APRON,FLAMERESISTANTCLOTHING,OTHERPPEDESCRIPTION, "
					+" NEEDFOULWEATHERGEAR,HAVECONSENTOFTASKLEADER,COMPANYOFTASKLEADER from IOP.JSA_PPE " 
					+" where PERMITNUMBER = :permitNum";
			logger.info("JSA_PPE sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsappeDto jsappeDto = new JsappeDto();
			for(Object[] a : obj){
				jsappeDto.setPermitNumber((Integer)a[0]);
				jsappeDto.setHardHat(Integer.parseInt(a[1].toString()));
				jsappeDto.setSafetyBoot(Integer.parseInt(a[2].toString()));
				jsappeDto.setGoggles(Integer.parseInt(a[3].toString()));
				jsappeDto.setFaceShield(Integer.parseInt(a[4].toString()));
				jsappeDto.setSafetyGlasses(Integer.parseInt(a[5].toString()));
				jsappeDto.setSingleEar(Integer.parseInt(a[6].toString()));
				jsappeDto.setDoubleEars(Integer.parseInt(a[7].toString()));
				jsappeDto.setRespiratorTypeDescription((String)a[8]);
				jsappeDto.setNeedScba(Integer.parseInt(a[9].toString()));
				jsappeDto.setNeedDustMask(Integer.parseInt(a[10].toString()));
				jsappeDto.setCottonGlove(Integer.parseInt(a[11].toString()));
				jsappeDto.setLeatherGlove(Integer.parseInt(a[12].toString()));
				jsappeDto.setImpactProtection(Integer.parseInt(a[13].toString()));
				jsappeDto.setGloveDescription((String)a[14]);
				jsappeDto.setChemicalGloveDescription((String)a[15]);
				jsappeDto.setFallProtection(Integer.parseInt(a[16].toString()));
				jsappeDto.setFallRestraint(Integer.parseInt(a[17].toString()));
				jsappeDto.setChemicalSuit(Integer.parseInt(a[18].toString()));
				jsappeDto.setApron(Integer.parseInt(a[19].toString()));
				jsappeDto.setFlameResistantClothing(Integer.parseInt(a[20].toString()));
				jsappeDto.setOtherppeDescription((String)a[21]);
				jsappeDto.setNeedFoulWeatherGear((String)a[22]); // need to convert this into string as per xsjs payload
				jsappeDto.setHaveConsentOfTaskLeader(Integer.parseInt(a[23].toString()));
				jsappeDto.setCompanyOfTaskLeader((String)a[24]);
			}
			logger.info(jsappeDto.toString());
			getJsaByPermitNumPayloadDto.setTOJSE_PPE(jsappeDto);
			//////////////////////////////////////////////////////////////
			sql = "select distinct PERMITNUMBER, PRESURIZEDEQUIPMENT,PERFORMISOLATION,DEPRESSURIZEDRAIN, "
					+" RELIEVETRAPPEDPRESSURE,DONOTWORKINLINEOFFIRE,ANTICIPATERESIDUAL,SECUREALLHOSES "
					+" from IOP.JSAHAZARDSPRESSURIZED where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPRESSURIZED sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsPressurizedDto jsaHazardsPressurizedDto = new JsaHazardsPressurizedDto();
			for(Object[] a : obj){
				jsaHazardsPressurizedDto.setPermitNumber((Integer)a[0]);
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
					+" WAITUNTILVISIBILITYIMPROVE,DEFERUNTILVISIBILITYIMPROVE,KNOWDISTANCEFROMPOLES "
					+" from IOP.JSAHAZARDSVISIBILITY where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSVISIBILITY sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsVisibilityDto jsaHazardsVisibilityDto = new JsaHazardsVisibilityDto();
			for(Object[] a : obj){
				jsaHazardsVisibilityDto.setPermitNumber((Integer)a[0]);
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
					+" VERIFYCOMPETENCIES,ADDRESSLIMITATIONS,MANAGELANGUAGEBARRIERS,WEARSEATBELTS "
					+" from IOP.JSAHAZARDSPERSONNEL where PERMITNUMBER = :permitNum";
			logger.info("JSAHAZARDSPERSONNEL sql " + sql);
			q = getSession().createNativeQuery(sql);
			q.setParameter("permitNum", permitNum);
			obj = q.getResultList();
			JsaHazardsPersonnelDto jsaHazardsPersonnelDto = new JsaHazardsPersonnelDto();
			for(Object[] a : obj){
				jsaHazardsPersonnelDto.setPermitNumber((Integer)a[0]);
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
			
			//////////////////////////////////////////////////////////////
			logger.info("GetJsaByPermitNumDao | Final Output" + getJsaByPermitNumPayloadDto);
			return getJsaByPermitNumPayloadDto;
		} catch (Exception e) {
			logger.error("GetJsaByPermitNumDao | getJsaByPermitNum  error" + e.getMessage());
		}
		return null;

	}

}
