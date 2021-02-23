package com.murphy.taskmgmt.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.dto.DowntimeCapturedCADto;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.taskmgmt.dao.DowntimeCapturedDao;
import com.murphy.taskmgmt.dao.DowntimeWellParentCodeDao;
import com.murphy.taskmgmt.dao.WellMuwiDao;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
//import com.murphy.taskmgmt.dto.DowntimeCapturedCADto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.Response;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DowntimeCapturedFacadeCALocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("DowntimeCaptureCaFacade")
public class DowntimeCaptureCaFacade  implements DowntimeCapturedFacadeCALocal{

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapturedFacade.class);

	@Autowired
	private DowntimeWellParentCodeDao downtimeWellParentCodeDao;
	
	@Autowired
	private DowntimeCapturedDao downtimeCaptureDao;
	
	@Autowired
	WellMuwiDao wellMuwiDao;
	
	@Override
	public ResponseMessage createDowntimeCapturedForCanada(DowntimeUpdateDto downTmeCanada) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Downtime " + MurphyConstant.FAILURE);

		if (!ServicesUtil.isEmpty(downTmeCanada.getDto().getMuwi()) && !ServicesUtil.isEmpty(downTmeCanada.getDto().getCreatedAt())
				&& (!ServicesUtil.isEmpty(downTmeCanada.getDto().getPointId())) || downTmeCanada.getIsProCountUpdate().equals(true)) {

			String id = UUID.randomUUID().toString().replaceAll("-", "");
			downTmeCanada.getDto().setId(id);
			responseDto = downtimeCaptureDao.createDowntimeCaptured(downTmeCanada);
			if (!ServicesUtil.isEmpty(downTmeCanada.getIsProCountUpdate()) && downTmeCanada.getIsProCountUpdate().equals(true)) {
				try {
					if (responseDto.getStatus().equals(MurphyConstant.SUCCESS)) {
						logger.error("[Murphy][DowntimeCaptureDao][Create][PROD_VIEW][start updating PROD VIEW]");
						ResponseMessage message = updateProdViewDb(downTmeCanada);
						if (message.getStatus().equals("true")){
							responseDto.setMessage("Downtime " + MurphyConstant.CREATED_SUCCESS);
						    responseDto.setStatus(MurphyConstant.SUCCESS);
						    responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
						}
						else {
							responseDto.setMessage(message.getMessage());
							responseDto.setStatus(MurphyConstant.FAILURE);
							responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
						}
					}
				} catch (Exception e) {
					logger.error("[Murphy][DowntimeCaptureDao][Create][ERROR]" + e.getMessage());
				}
			}

		} else {
			responseDto.setMessage(MurphyConstant.MAND_MISS);
			responseDto.setStatus(MurphyConstant.FAILURE);
			responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseDto;
	}

	private ResponseMessage updateProdViewDb(DowntimeUpdateDto downTmeCanada) {
		ResponseMessage responseMessage = new ResponseMessage();
		/* Update ProCount */
		String result = "false";
		try {
			String getTotalDuration = downtimeCaptureDao.getDuration(downTmeCanada.getDto(),downTmeCanada.getCountryCode());
			int rocMinute = 0;
			int rocHour = 0;
			if (!ServicesUtil.isEmpty(getTotalDuration)) {
				rocMinute = Integer.parseInt(getTotalDuration);
				rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
			}
			else{
				rocHour=downTmeCanada.getDto().getDurationByRocHour();
				rocMinute=downTmeCanada.getDto().getDurationByRocMinute();
			}

			logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][roc_hour] & [roc_minute]" + rocHour + rocMinute);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat procountDateFormat = new SimpleDateFormat(MurphyConstant.Murphy_DOWNTIME_DATE_PROCOUNT);
			//Date in UTC being converted to MST for PROCOUNT
			//SOC
			String createdAt = downTmeCanada.getDto().getCreatedAt();
			logger.error("Line 164 [CreatedAT UTC]: "+createdAt);
			
			String location=ServicesUtil.getLocationByLocationCode(wellMuwiDao.getLocationCodeByMUWI(downTmeCanada.getDto().getMuwi()));
			String originalDate=null;
			if(MurphyConstant.KAY_BASE_LOC_CODE.equalsIgnoreCase(location)){
			 originalDate = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.UTC_ZONE,
					MurphyConstant.MST_ZONE, MurphyConstant.DATE_DB_FORMATE_SD,
					MurphyConstant.DATE_DB_FORMATE_SD);
			logger.error("Line 168  [CreatedAT MST]: "+originalDate);
			}else{
			originalDate = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.UTC_ZONE,
					MurphyConstant.PST_ZONE, MurphyConstant.DATE_DB_FORMATE_SD,
					MurphyConstant.DATE_DB_FORMATE_SD);
					logger.error("Line 168  [CreatedAT PST]: "+originalDate);	
			}
			//EOC

			try {

				Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalDate);
				Boolean time = new DowntimeCapturedDao().timeBetweenOrNot(dateNew,MurphyConstant.CA_CODE);
				logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][dateNew]" + dateNew);
				logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][time]" + time);
				if (time.equals(true)) {
					originalDate = procountDateFormat.format(ServicesUtil.getPrevDate(dateNew));
					logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][getPrevDate- originalDate]" + originalDate);
				} else {
					originalDate = procountDateFormat.format(dateFormat.parse(originalDate));
					logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][originalDate]" + originalDate);
				}

			} catch (ParseException e) {
				logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][dateFormatException] : " + e.getMessage());
			}
			downTmeCanada.getDto().setDurationByRocHour(rocHour);
			downTmeCanada.getDto().setDurationByRocMinute(rocMinute);
			downTmeCanada.getDto().setCreatedAt(originalDate);
			logger.error("downTmeCanadaPrint Dto" + downTmeCanada.getDto());
			DowntimeCaptureLocal downtime = new DowntimeCapture();
			com.murphy.integration.dto.ResponseMessage insertOrUpdateCounts = downtime.insertOrUpdateDataToProdView(convertToProdDto(downTmeCanada),location);
			responseMessage.setMessage(insertOrUpdateCounts.getMessage());
			responseMessage.setStatus(insertOrUpdateCounts.getStatus());
			result = insertOrUpdateCounts.getStatus();
			logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][result]" + result);
            return responseMessage;
		} catch (Exception e) {
			responseMessage.setMessage("Prod View Update Failed");
			responseMessage.setStatus("false");
			logger.error("[Murphy][DowntimeCaptureDao][PROD_VIEW][ERROR]" + e.getMessage());
		}
		return responseMessage;
	}

	private DowntimeCaptureDto convertDto(DowntimeUpdateDto downTmeCanada) {
		DowntimeCaptureDto proCountDto = new DowntimeCaptureDto();
		DowntimeCapturedDto dto = downTmeCanada.getDto();
		proCountDto.setUwiId(ServicesUtil.isEmpty(dto.getMuwi()) ? null : dto.getMuwi());
		proCountDto.setChildCode(
				ServicesUtil.isEmpty(dto.getChildCode().toString()) ? null : dto.getChildCode().toString());
		proCountDto.setComments("");
		proCountDto.setDurationInHours(
				ServicesUtil.isEmpty(dto.getDurationByRocHour()) ? null : dto.getDurationByRocHour());
		proCountDto.setDurationInMinutes(
				(ServicesUtil.isEmpty(dto.getDurationByRocMinute()) ? null : dto.getDurationByRocMinute()));
		proCountDto.setOriginalDateEntered(ServicesUtil.isEmpty(dto.getCreatedAt()) ? null
				: ServicesUtil.convertFromZoneToZone(null, dto.getCreatedAt(), "", "", MurphyConstant.DATEFORMAT_T,
						MurphyConstant.DATE_DB_FORMATE_SD));
		proCountDto.setParentCode(dto.getDowntimeCode().toString());
		proCountDto.setStartDate(ServicesUtil.isEmpty(dto.getCreatedAt()) ? null
				: ServicesUtil.convertFromZoneToZone(null, dto.getCreatedAt(), "", "", MurphyConstant.DATEFORMAT_T,
						MurphyConstant.DATE_DB_FORMATE_SD)); 
		logger.error("convertDto Set Start Date "+proCountDto.getStartDate() + " Convert CreatedAt Date "+dto.getCreatedAt());
		//convertDto Set Start Date Fri Aug 16 10:51:00 UTC 2019 Convert CreatedAt Date 2019-08-16T10:51:57
		//convertDto Set Start Date Fri Aug 16 11:40:00 UTC 2019 Convert CreatedAt Date 2019-08-16T11:40:12

		return proCountDto;
	}

	
	private DowntimeCaptureDto convertToProdDto(DowntimeUpdateDto downtimeDto) {
		logger.error("Inside convertToProdDto");
		DowntimeCaptureDto prodViewDto = new DowntimeCaptureDto();
		DowntimeCapturedDto dto = downtimeDto.getDto();
		prodViewDto.setUwiId(ServicesUtil.isEmpty(dto.getMuwi()) ? null : dto.getMuwi());
		prodViewDto.setComments("");
		prodViewDto.setDurationInHours(
				ServicesUtil.isEmpty(dto.getDurationByRocHour()) ? null : dto.getDurationByRocHour());
		prodViewDto.setDurationInMinutes(
				(ServicesUtil.isEmpty(dto.getDurationByRocMinute()) ? null : dto.getDurationByRocMinute()));
		prodViewDto.setOriginalDateEntered(ServicesUtil.isEmpty(dto.getCreatedAt()) ? null
				: ServicesUtil.convertFromZoneToZone(null, dto.getCreatedAt(), "", "", MurphyConstant.DATEFORMAT_T,
						MurphyConstant.DATE_DB_FORMATE));
		prodViewDto.setCodeDownTime1(dto.getDowntimeCode().toString());
		prodViewDto.setCodeDownTime2(ServicesUtil.isEmpty(dto.getDowntimeText()) ? null : dto.getDowntimeText().toString());
		prodViewDto.setStartDate((ServicesUtil.isEmpty(dto.getCreatedAt()) ? null
				: ServicesUtil.convertFromZoneToZone(null, dto.getCreatedAt(), "", "", MurphyConstant.DATEFORMAT_T,
						MurphyConstant.DATE_DB_FORMATE_SD))); 
//		
		logger.error("prodViewDto "+prodViewDto+ " Convert CreatedAt Date "+prodViewDto.getUwiId());
		//convertDto Set Start Date Fri Aug 16 10:51:00 UTC 2019 Convert CreatedAt Date 2019-08-16T10:51:57
		//convertDto Set Start Date Fri Aug 16 11:40:00 UTC 2019 Convert CreatedAt Date 2019-08-16T11:40:12

		return prodViewDto;
	}
	
	
	@Override
	public ResponseEntity<Response<List<DowntimeWellParentCodeDto>>> getActiveParenCodeForWellDowntime() {
		Response<List<DowntimeWellParentCodeDto>> response=new Response<List<DowntimeWellParentCodeDto>>();
		List<DowntimeWellParentCodeDto> parentCodeList=new ArrayList<>();
		try{
		parentCodeList=downtimeWellParentCodeDao.getActiveParentCodeForCaWellDowntime();
		response.setOutput(parentCodeList);
		}
		catch(Exception e){
			logger.error("[Murphy][DowntimeCaptureCaFacade][getActiveParenCodeForWellDowntime][Exception]"+e.getMessage());
			response.setStatus(MurphyConstant.FAILURE);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public DowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet) {
		DowntimeResponseDto dto = new DowntimeResponseDto();
		DowntimeRequestDto dtoGetNew = new DowntimeRequestDto();
		List<LocationHierarchyDto> locationHierarchyDto = new ArrayList<LocationHierarchyDto>();
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		boolean isWellCheck = false;
		
		//Location History
		if(!ServicesUtil.isEmpty(dtoGet.getMonthTime()))
		{
			dtoGetNew.setLocationType(dtoGet.getLocationType());
			dtoGetNew.setMonthTime(dtoGet.getMonthTime());
			dtoGetNew.setPage(dtoGet.getPage());
			dtoGetNew.setPage_size(dtoGet.getPage_size());
			dtoGetNew.setStatusType(dtoGet.getStatusType());
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {
				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.WELL) ||
						locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.SEARCH)	) {
					isWellCheck = true;
					locationHierarchyDto.add(locationDto);
				}
			}
			dtoGetNew.setLocationHierarchy(locationHierarchyDto);
			if(isWellCheck){
		    	dto = downtimeCaptureDao.getDowntime(dtoGetNew);
		    }
			else
		    {
		    	dto.setDtoList(null);
		    	dto.setTotalCount(BigDecimal.ZERO);
		    	dto.setPageCount(new BigDecimal(dtoGet.getPage_size()));
			}
		}
		else 
		{
			if (!ServicesUtil.isEmpty(dtoGet.getStatusType())) {
				dto = downtimeCaptureDao.getDowntime(dtoGet);
			}
		}
		
		
		if (!ServicesUtil.isEmpty(dto.getDtoList())) 
		{
			message.setMessage("Downtime :" + MurphyConstant.EXISTS);
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			dto.setMessage(message);
		}
		else 
		{
			boolean isNotWell = false;
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {		
				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.FLARE) 
					|| locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.COMPRESSOR)) {
					isNotWell = true;
					break;
				}
			}
			if (isNotWell) {
				message.setMessage("Error : Incorrect Component selected. Please select Well");
				dto.setMessage(message);
			}
			else {
				message.setMessage("Downtime :" + MurphyConstant.NO_RECORD);
				dto.setMessage(message);
			}
		
		}
		return dto;
	
	}

	@Override
	public ResponseMessage updateDowntimeCaptured(DowntimeUpdateDto downtimeDto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			if (!ServicesUtil.isEmpty(downtimeDto.getDto())) {

				String result = downtimeCaptureDao.updateCaDowntimeCaptured(downtimeDto.getDto());
				if (result.equals(MurphyConstant.SUCCESS)) {
					if (!ServicesUtil.isEmpty(downtimeDto.getIsProCountUpdate())
							&& downtimeDto.getIsProCountUpdate().equals(true)) {
						
						logger.error("[DowntimeCaptureDao][PROD_VIEW][start updating PRO COUNT][Created At: ] "+downtimeDto.getDto().getCreatedAt());
						
						ResponseMessage message = updateProdViewDb(downtimeDto);
						if (message.getStatus().equals("true")) {
							responseDto.setMessage("Downtime " + MurphyConstant.UPDATE_SUCCESS);
							responseDto.setStatus(MurphyConstant.SUCCESS);
							responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
						} else {
							responseDto.setMessage(message.getMessage());
							responseDto.setStatus(MurphyConstant.FAILURE);
							responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
						}
					} else {
						responseDto.setMessage("Downtime " + MurphyConstant.UPDATE_SUCCESS);
						responseDto.setStatus(MurphyConstant.SUCCESS);
						responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
					}

				} else {
					responseDto.setMessage("Couldnt find a record with the id");
				}
			} else {
				responseDto.setMessage(MurphyConstant.MAND_MISS);
			}
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCaptureDao][Error]" + e.getMessage());
		}
		return responseDto;
	}

}
