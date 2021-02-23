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
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.DowntimeCaptureDto;
import com.murphy.integration.interfaces.DowntimeCaptureLocal;
import com.murphy.integration.service.DowntimeCapture;
import com.murphy.taskmgmt.dao.DowntimeCapturedDao;
import com.murphy.taskmgmt.dao.DowntimeWellChildCodeDao;
import com.murphy.taskmgmt.dao.DowntimeWellParentCodeDao;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.DowntimeWellChildCodeDto;
import com.murphy.taskmgmt.dto.DowntimeWellChildCodeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeDto;
import com.murphy.taskmgmt.dto.DowntimeWellParentCodeResponseDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.DowntimeCapturedFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("DowntimeCaptureFacade")
public class DowntimeCapturedFacade implements DowntimeCapturedFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapturedFacade.class);

	public DowntimeCapturedFacade() {
	}

	@Autowired
	private DowntimeCapturedDao downtimeCaptureDao;
	
	@Autowired
	private DowntimeWellParentCodeDao downtimeWellParentCodeDao;
	
	@Autowired
	private DowntimeWellChildCodeDao downtimeWellChildCodeDao;

	@Override
	public ResponseMessage createDowntimeCaptured(DowntimeUpdateDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Downtime " + MurphyConstant.FAILURE);

		if (!ServicesUtil.isEmpty(dto.getDto().getMuwi()) && !ServicesUtil.isEmpty(dto.getDto().getCreatedAt())
				&& (!ServicesUtil.isEmpty(dto.getDto().getPointId())) || dto.getIsProCountUpdate().equals(true)) {

			String id = UUID.randomUUID().toString().replaceAll("-", "");
			dto.getDto().setId(id);
			responseDto = downtimeCaptureDao.createDowntimeCaptured(dto);
			if (!ServicesUtil.isEmpty(dto.getIsProCountUpdate()) && dto.getIsProCountUpdate().equals(true)) {
				try {
					if (responseDto.getStatus().equals(MurphyConstant.SUCCESS)) {
						logger.error("[Murphy][DowntimeCaptureDao][Create][PRO_COUNT][start updating PRO COUNT]");
						String proCountResult = updateProCountDb(dto);
						if (proCountResult.equals("true"))
							responseDto.setMessage("Downtime " + MurphyConstant.CREATED_SUCCESS);
						else {
							responseDto.setMessage("Pro Count Update Failed");
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
						
						logger.error("[DowntimeCaptureDao][PRO_COUNT][start updating PRO COUNT][Created At: ] "+downtimeDto.getDto().getCreatedAt());
						/*String createdAt = downtimeDto.getDto().getCreatedAt();
						createdAt = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.UTC_ZONE,
								MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE_SD,
								MurphyConstant.DATE_DB_FORMATE_SD);*/
						//downtimeDto.getDto().setCreatedAt(createdAt);
						//logger.error("[PRO_COUNT][Created At:][CST] "+createdAt);
						
						String proCountResult = updateProCountDb(downtimeDto);
						if (proCountResult.equals("true")) {
							responseDto.setMessage("Downtime " + MurphyConstant.UPDATE_SUCCESS);
							responseDto.setStatus(MurphyConstant.SUCCESS);
							responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
						} else {
							responseDto.setMessage("Pro Count Update Failed");
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

	public String updateProCountDb(DowntimeUpdateDto downtimeDto) {

		com.murphy.integration.dto.ResponseMessage insertOrUpdateCounts = null;
		/* Update ProCount */
		String result = "false";
		try {
			String getTotalDuration = downtimeCaptureDao.getDuration(downtimeDto.getDto(),downtimeDto.getCountryCode());
			int rocMinute = 0;
			int rocHour = 0;
			if (!ServicesUtil.isEmpty(getTotalDuration)) {
				rocMinute = Integer.parseInt(getTotalDuration);
				rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
			}

			logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][roc_hour] & [roc_minute]" + rocHour + rocMinute);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat procountDateFormat = new SimpleDateFormat(MurphyConstant.Murphy_DOWNTIME_DATE_PROCOUNT);
			//Date in UTC being converted to CST for PROCOUNT
			//SOC
			String createdAt = downtimeDto.getDto().getCreatedAt();
			logger.error("Line 164 [CreatedAT UTC]: "+createdAt);
			String originalDate = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.UTC_ZONE,
					MurphyConstant.CST_ZONE, MurphyConstant.DATE_DB_FORMATE_SD,
					MurphyConstant.DATE_DB_FORMATE_SD);
			logger.error("Line 168  [CreatedAT CST]: "+originalDate);
			//EOC

			try {

				Date dateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalDate);
				Boolean time = new DowntimeCapturedDao().timeBetweenOrNot(dateNew,MurphyConstant.EFS_CODE);
				logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][dateNew]" + dateNew);
				logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][time]" + time);
				if (time.equals(true)) {
					originalDate = procountDateFormat.format(ServicesUtil.getPrevDate(dateNew));
					logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][getPrevDate- originalDate]" + originalDate);
				} else {
					originalDate = procountDateFormat.format(dateFormat.parse(originalDate));
					logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][originalDate]" + originalDate);
				}

			} catch (ParseException e) {
				logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][dateFormatException] : " + e.getMessage());
			}
			downtimeDto.getDto().setDurationByRocHour(rocHour);
			downtimeDto.getDto().setDurationByRocMinute(rocMinute);
			downtimeDto.getDto().setCreatedAt(originalDate);

			DowntimeCaptureLocal downtime = new DowntimeCapture();
			insertOrUpdateCounts = downtime.insertOrUpdateCounts(convertDto(downtimeDto));

			result = insertOrUpdateCounts.getStatus();
			logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][result]" + result);

		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCaptureDao][PRO_COUNT][ERROR]" + e.getMessage());
		}
		return result;
	}

	private DowntimeCaptureDto convertDto(DowntimeUpdateDto downtimeDto) {
		DowntimeCaptureDto proCountDto = new DowntimeCaptureDto();
		DowntimeCapturedDto dto = downtimeDto.getDto();
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
	
	//added as a part of Data Maintenance - sprint 5
	//SOC
	public DowntimeWellParentCodeResponseDto getActiveParenCodeForWellDowntime(String country) {
		


		DowntimeWellParentCodeResponseDto responseDto = new DowntimeWellParentCodeResponseDto();
		List<DowntimeWellParentCodeDto> downtimeWellParentCodeDtoList = new ArrayList<DowntimeWellParentCodeDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			downtimeWellParentCodeDtoList = downtimeWellParentCodeDao.getActiveParenCodeForWellDowntime(country);
			if (!ServicesUtil.isEmpty(downtimeWellParentCodeDtoList)) {
				responseDto.setDowntimeWellParentCodeDtoList(downtimeWellParentCodeDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedFacade][getActiveParenCodeForWellDowntime][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;

	
	}
	
	public DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntime() {


		DowntimeWellChildCodeResponseDto responseDto = new DowntimeWellChildCodeResponseDto();
		List<DowntimeWellChildCodeDto> downtimeWellChildCodeDtoList = new ArrayList<DowntimeWellChildCodeDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			downtimeWellChildCodeDtoList =  downtimeWellChildCodeDao.getActiveChildCodeForWellDowntime(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(downtimeWellChildCodeDtoList)) {
				responseDto.setDowntimeWellChildCodeDtoList(downtimeWellChildCodeDtoList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedFacade][getActiveChildCodeForWellDowntime][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;

	
		
	}
	//EOC

	@Override
	public  DowntimeWellChildCodeResponseDto getActiveChildCodeForWellDowntimeByParentCode(String parentCode) {


		DowntimeWellChildCodeResponseDto responseDto = new DowntimeWellChildCodeResponseDto();
		List<DowntimeWellChildCodeDto> downtimeWellChildCodeDtoList = new ArrayList<DowntimeWellChildCodeDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			downtimeWellChildCodeDtoList = downtimeWellChildCodeDao.getActiveChildCodeForWellDowntimeByParentCode(MurphyConstant.ACTIVE, parentCode);
			if (!ServicesUtil.isEmpty(downtimeWellChildCodeDtoList)) {
				responseDto.setDowntimeWellChildCodeDtoList(downtimeWellChildCodeDtoList);

				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][DowntimeCapturedFacade][getActiveChildCodeForWellDowntimeByParentCode][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;
	}
	
	
	//added for location History in case of canada muwis
	
	@Override
	public DowntimeResponseDto getDowntimeHierarchyForCanada(DowntimeRequestDto dtoGet) {
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
			dtoGetNew.setCountryCode(dtoGet.getCountryCode());
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {
				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.WELL) ||
						locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.SEARCH)	) {
					isWellCheck = true;
					locationHierarchyDto.add(locationDto);
				}
			}
			dtoGetNew.setLocationHierarchy(locationHierarchyDto);
			if(isWellCheck){
				
		    	dto = downtimeCaptureDao.getLocationHistoryForDowntimeInCanda(dtoGetNew);
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
				dto = downtimeCaptureDao.getLocationHistoryForDowntimeInCanda(dtoGet);
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
	
}
