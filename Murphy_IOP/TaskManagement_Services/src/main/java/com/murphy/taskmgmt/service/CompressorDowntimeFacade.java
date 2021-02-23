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
import com.murphy.integration.interfaces.CompressorDowntimeCaptureLocal;
import com.murphy.integration.service.CompressorDowntimeCapture;
import com.murphy.taskmgmt.dao.CompressorDowntimeDao;
import com.murphy.taskmgmt.dao.DowntimeCapturedDao;
import com.murphy.taskmgmt.dto.DowntimeCapturedDto;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.DowntimeResponseDto;
import com.murphy.taskmgmt.dto.DowntimeUpdateDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.CompressorDowntimeFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("CompressorDowntimeFacade")
public class CompressorDowntimeFacade implements CompressorDowntimeFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(CompressorDowntimeFacade.class);

	public CompressorDowntimeFacade() {
	}

	@Autowired
	private CompressorDowntimeDao compressorDowntimeDao;

	@Override
	public ResponseMessage createCompressorDowntime(DowntimeUpdateDto dto) {

		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Downtime " + MurphyConstant.FAILURE);

		if (!ServicesUtil.isEmpty(dto.getDto().getMuwi()) && !ServicesUtil.isEmpty(dto.getDto().getCreatedAt())
				&& (!ServicesUtil.isEmpty(dto.getDto().getPointId())) || dto.getIsProCountUpdate().equals(true)) {

			String id = UUID.randomUUID().toString().replaceAll("-", "");
			dto.getDto().setId(id);
			responseDto = compressorDowntimeDao.createCompressorDowntime(dto);
			if (!ServicesUtil.isEmpty(dto.getIsProCountUpdate()) && dto.getIsProCountUpdate().equals(true)) {
				try {
					if (responseDto.getStatus().equals(MurphyConstant.SUCCESS)) {
						logger.error("[Murphy][CompressorDowntimeFacade][Create][PRO_COUNT][start updating PRO COUNT]");
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
					logger.error("[Murphy][CompressorDowntimeFacade][Create][ERROR]" + e.getMessage());
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
	public ResponseMessage updateCompressorDowntime(DowntimeUpdateDto downtimeDto) {

		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);

		try {
			if (!ServicesUtil.isEmpty(downtimeDto.getDto())) {

				String result = compressorDowntimeDao.updateCompressorDowntime(downtimeDto.getDto());
				if (result.equals(MurphyConstant.SUCCESS)) {
					if (!ServicesUtil.isEmpty(downtimeDto.getIsProCountUpdate())
							&& downtimeDto.getIsProCountUpdate().equals(true)) {
						logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][start updating PRO COUNT][Created At: ] "+downtimeDto.getDto().getCreatedAt());
						//String createdAt = downtimeDto.getDto().getCreatedAt();
						/*createdAt = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.CST_ZONE,
								MurphyConstant.CST_ZONE, MurphyConstant.DATE_DISPLAY_FORMAT,
								MurphyConstant.DATE_DB_FORMATE_SD); */
						// Convert to UTC time zone
						/*createdAt = ServicesUtil.convertFromZoneToZoneString(null, createdAt, MurphyConstant.CST_ZONE,
								MurphyConstant.UTC_ZONE, MurphyConstant.DATE_DISPLAY_FORMAT,
								MurphyConstant.DATE_DB_FORMATE_SD);
						downtimeDto.getDto().setCreatedAt(createdAt);*/

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
			logger.error("[Murphy][CompressorDowntimeFacade][Error]" + e.getMessage());
		}
		return responseDto;
	}

	public String updateProCountDb(DowntimeUpdateDto downtimeDto) {

		com.murphy.integration.dto.ResponseMessage insertOrUpdateCounts = null;
		/* Update ProCount */
		String result = "false";
		try {
			String getTotalDuration = compressorDowntimeDao.getDuration(downtimeDto.getDto());
			int rocMinute = 0;
			int rocHour = 0;
			if (!ServicesUtil.isEmpty(getTotalDuration)) {
				rocMinute = Integer.parseInt(getTotalDuration);
				rocHour = rocMinute / 60;
				rocMinute = rocMinute - (rocHour * 60);
			}

			logger.error(
					"[Murphy][CompressorDowntimeFacade][PRO_COUNT][roc_hour] & [roc_minute]" + rocHour + rocMinute);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat procountDateFormat = new SimpleDateFormat(MurphyConstant.Murphy_DOWNTIME_DATE_PROCOUNT);
			//String originalDate = downtimeDto.getDto().getCreatedAt();
			
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
				logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][dateNew]" + dateNew);
				logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][time]" + time);
				if (time.equals(true)) {
					originalDate = procountDateFormat.format(ServicesUtil.getPrevDate(dateNew));
					logger.error(
							"[Murphy][CompressorDowntimeFacade][PRO_COUNT][getPrevDate- originalDate]" + originalDate);
				} else {
					originalDate = procountDateFormat.format(dateFormat.parse(originalDate));
					logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][originalDate]" + originalDate);
				}

			} catch (ParseException e) {
				logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][dateFormatException] : " + e.getMessage());
			}
			downtimeDto.getDto().setDurationByRocHour(rocHour);
			downtimeDto.getDto().setDurationByRocMinute(rocMinute);
			downtimeDto.getDto().setCreatedAt(originalDate);

			CompressorDowntimeCaptureLocal downtime = new CompressorDowntimeCapture();
			insertOrUpdateCounts = downtime.insertOrUpdateCounts(convertDto(downtimeDto));

			result = insertOrUpdateCounts.getStatus();
			logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][result]" + result);

		} catch (Exception e) {
			logger.error("[Murphy][CompressorDowntimeFacade][PRO_COUNT][ERROR]" + e.getMessage());
		}
		return result;
	}

	private DowntimeCaptureDto convertDto(DowntimeUpdateDto downtimeDto) {
		DowntimeCaptureDto proCountDto = new DowntimeCaptureDto();
		DowntimeCapturedDto dto = downtimeDto.getDto();

		logger.error("Downtime code" + dto.getDowntimeCode() + "Child code" + dto.getChildCode());
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
		// proCountDto.setParentCode(dto.getDowntimeCode().toString());
		proCountDto.setStartDate(ServicesUtil.isEmpty(dto.getCreatedAt()) ? null
				: ServicesUtil.convertFromZoneToZone(null, dto.getCreatedAt(), "", "", MurphyConstant.DATEFORMAT_T,
						MurphyConstant.DATE_DB_FORMATE_SD));

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
		boolean isCompressorCheck = false;

		// Location History
		if (!ServicesUtil.isEmpty(dtoGet.getMonthTime())) {
			dtoGetNew.setLocationType(dtoGet.getLocationType());
			dtoGetNew.setMonthTime(dtoGet.getMonthTime());
			dtoGetNew.setPage(dtoGet.getPage());
			dtoGetNew.setPage_size(dtoGet.getPage_size());
			dtoGetNew.setStatusType(dtoGet.getStatusType());
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {

				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.COMPRESSOR)) {
					locationHierarchyDto.add(locationDto);
					isCompressorCheck = true;
				}
			}
			dtoGetNew.setLocationHierarchy(locationHierarchyDto);
			if (isCompressorCheck) {
				dto = compressorDowntimeDao.getCompressorDowntime(dtoGetNew);
			} else {
				dto.setDtoList(null);
				dto.setTotalCount(BigDecimal.ZERO);
				dto.setPageCount(new BigDecimal(dtoGet.getPage_size()));
			}
		} 
		else {
			if (!ServicesUtil.isEmpty(dtoGet.getStatusType())) {
				dto = compressorDowntimeDao.getCompressorDowntime(dtoGet);
			}
		}

		if (!ServicesUtil.isEmpty(dto.getDtoList())) {
			message.setMessage("Downtime :" + MurphyConstant.EXISTS);
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			// dto.setDtoList(list);
			dto.setMessage(message);

		} 
		else {
			boolean isNotCompressor = false;
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {

				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.FLARE)
						|| locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.WELL)) {
					isNotCompressor = true;
					break;
				}
			}
		    if (isNotCompressor) {
				message.setMessage(" Incorrect Component selected. Please select Compressor");
				dto.setMessage(message);
			} else {
				message.setMessage("Downtime :" + MurphyConstant.NO_RECORD);
				dto.setMessage(message);
			}
		}
		return dto;
	}

	@Override
	public DowntimeResponseDto getCompressorDowntimeCodes() {
		DowntimeResponseDto responseDto = new DowntimeResponseDto();
		List<DowntimeCapturedDto> downtimeList = new ArrayList<DowntimeCapturedDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
			downtimeList =  compressorDowntimeDao.getCompressorDowntimeCodes(MurphyConstant.ACTIVE);
			if(!ServicesUtil.isEmpty(downtimeList)){
		
			if (!ServicesUtil.isEmpty(downtimeList)) {
				responseDto.setDtoList(downtimeList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		}
			}
		catch (Exception e) {
			logger.error("[Murphy][TaskManagementFacade][getFLSOP][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;

	}

}
