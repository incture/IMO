package com.murphy.taskmgmt.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.integration.dto.FlareDowntimeCaptureDto;
import com.murphy.integration.interfaces.FlareDowntimeCaptureLocal;
import com.murphy.integration.service.FlareDowntimeCapture;
import com.murphy.taskmgmt.dao.FlareDowntimeCaptureDao;
import com.murphy.taskmgmt.dto.DowntimeRequestDto;
import com.murphy.taskmgmt.dto.FlareCodeDto;
import com.murphy.taskmgmt.dto.FlareCodeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeResponseDto;
import com.murphy.taskmgmt.dto.FlareDowntimeUpdateDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.FlareDowntimeCaptureFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("FlareDowntimeCaptureFacade")
public class FlareDowntimeCaptureFacade implements FlareDowntimeCaptureFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(DowntimeCapturedFacade.class);

	public FlareDowntimeCaptureFacade() {
	}

	@Autowired
	private FlareDowntimeCaptureDao flareDowntimeCaptureDao;

	@Override
	public ResponseMessage createFlareDowntime(FlareDowntimeUpdateDto dto) {

		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		responseDto.setMessage("Flare Downtime " + MurphyConstant.FAILURE);

		if (!ServicesUtil.isEmpty(dto.getDto().getMerrickId()) && !ServicesUtil.isEmpty(dto.getDto().getCreatedAt())
				|| dto.getIsProCountUpdate().equals(true)) {

			String id = UUID.randomUUID().toString().replaceAll("-", "");
			dto.getDto().setId(id);
			// dto.getDto().setCreatedAt(dto.getDto().getCreatedAt() +
			// "00:00:00");
			responseDto = flareDowntimeCaptureDao.createFlareDowntime(dto);
			if (!ServicesUtil.isEmpty(dto.getIsProCountUpdate()) && dto.getIsProCountUpdate().equals(true)) {
				try {
					if (responseDto.getStatus().equals(MurphyConstant.SUCCESS)) {
						logger.error(
								"[Murphy][FlareDowntimeCaptureFacade][Create][PRO_COUNT][start updating PRO COUNT][Created At: ] "+dto.getDto().getCreatedAt());

						FlareDowntimeCaptureLocal downtime = new FlareDowntimeCapture();
						FlareDowntimeCaptureDto procountDto = new FlareDowntimeCaptureDto();
						Date created_Date = flareDowntimeCaptureDao.getCreatedAt(dto.getDto());
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						format1.setTimeZone(TimeZone.getTimeZone(MurphyConstant.CST_ZONE));
						String dateStr = format1.format(created_Date);

						// to get yesteday's date for Production Date
						// SOC : Incident INC0077904
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						cal.setTime(created_Date);
						cal.add(Calendar.DATE, -1);
						String dateString = sdf.format(cal.getTime());
						procountDto.setProductionDate(dateString + " 00:00:00");
						// EOC : Incident INC0077904

						procountDto.setFlareCode(dto.getDto().getChildCode());
						procountDto.setFlareVolume(dto.getDto().getFlareVolume().floatValue());
						procountDto.setMerrickId(Integer.parseInt(dto.getDto().getMerrickId()));
						procountDto.setRecordDate(dateStr + " 00:00:00");
						logger.error("[Murphy][FlareDowntimeCaptureFacade][PRO_COUNT][MerrickId]"
								+ procountDto.getMerrickId() + "[Record Date]" + procountDto.getRecordDate());
						com.murphy.integration.dto.ResponseMessage proCountResult = downtime
								.insertOrUpdateFlareDowntime(procountDto);
						if (proCountResult.getStatus().equalsIgnoreCase("true"))
							responseDto.setMessage("Downtime " + MurphyConstant.CREATED_SUCCESS);
						else {
							responseDto.setMessage("Pro Count Update Failed");
							responseDto.setStatus(MurphyConstant.FAILURE);
							responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
						}
					}
				} catch (Exception e) {
					logger.error("[Murphy][FlareDowntimeCaptureFacade][Create][ERROR]" + e.getMessage());
				}
			} else {
				logger.error("[Murphy][FlareDowntimeCaptureFacade][Create][ERROR]" + "Procount Update requested false");
			}

		} else {
			responseDto.setMessage(MurphyConstant.MAND_MISS);
			responseDto.setStatus(MurphyConstant.FAILURE);
			responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		}
		return responseDto;
	}

	@Override
	public FlareCodeResponseDto getFlareDowntimeCodes() {
		FlareCodeResponseDto responseDto = new FlareCodeResponseDto();
		List<FlareCodeDto> downtimeList = new ArrayList<FlareCodeDto>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		responseMessage.setStatus(MurphyConstant.FAILURE);
		responseMessage.setStatusCode(MurphyConstant.CODE_FAILURE);
		try {
			downtimeList = flareDowntimeCaptureDao.getFlareDowntimeCodes(MurphyConstant.ACTIVE);
			if (!ServicesUtil.isEmpty(downtimeList)) {
				responseDto.setDtoList(downtimeList);
				responseMessage.setMessage(MurphyConstant.READ_SUCCESS);
			} else {
				responseMessage.setMessage(MurphyConstant.NO_RESULT);
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][FlareDowntimeCaptureFacade][getFlareDowntimeCodes][error]" + e.getMessage());
		}
		responseDto.setMessage(responseMessage);
		return responseDto;
	}

	@Override
	public FlareDowntimeResponseDto getDowntimeHierarchy(DowntimeRequestDto dtoGet) {
		FlareDowntimeResponseDto dto = new FlareDowntimeResponseDto();
		DowntimeRequestDto dtoGetNew = new DowntimeRequestDto();
		List<LocationHierarchyDto> locationHierarchyDto = new ArrayList<LocationHierarchyDto>();
		ResponseMessage message = new ResponseMessage();
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		boolean isFlareCheck = false;

		// Location History
		if (!ServicesUtil.isEmpty(dtoGet.getMonthTime())) {
			dtoGetNew.setLocationType(dtoGet.getLocationType());
			dtoGetNew.setMonthTime(dtoGet.getMonthTime());
			dtoGetNew.setPage(dtoGet.getPage());
			dtoGetNew.setPage_size(dtoGet.getPage_size());
			dtoGetNew.setStatusType(dtoGet.getStatusType());
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {
				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.FLARE)) {
					locationHierarchyDto.add(locationDto);
					isFlareCheck = true;
				}
			}
			dtoGetNew.setLocationHierarchy(locationHierarchyDto);

			if (isFlareCheck) {
				dto = flareDowntimeCaptureDao.getDowntime(dtoGetNew);
			} else {
				dto.setDtoList(null);
				dto.setTotalCount(BigDecimal.ZERO);
				dto.setPageCount(new BigDecimal(dtoGet.getPage_size()));
			}
		} else {
			if (!ServicesUtil.isEmpty(dtoGet.getStatusType())) {
				dto = flareDowntimeCaptureDao.getDowntime(dtoGet);
			}
		}

		if (!ServicesUtil.isEmpty(dto.getDtoList())) {
			message.setMessage("Flare Downtime :" + MurphyConstant.EXISTS);
			message.setStatus(MurphyConstant.SUCCESS);
			message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			// dto.setDtoList(list);
			dto.setMessage(message);
		} else {
			boolean isNotFlare = false;
			for (LocationHierarchyDto locationDto : dtoGet.getLocationHierarchy()) {

				if (locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.COMPRESSOR)
						|| locationDto.getLocationType().equalsIgnoreCase(MurphyConstant.WELL)) {
					isNotFlare = true;
					break;
				}
			}
			if (isNotFlare) {
				message.setMessage("Error : Incorrect Component selected. Please select Flare");
				dto.setMessage(message);
			} else {
				message.setMessage("Flare Downtime :" + MurphyConstant.NO_RECORD);
				dto.setMessage(message);
			}
		}

		return dto;
	}

	@Override
	public ResponseMessage updateFlareDowntime(FlareDowntimeUpdateDto dto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(MurphyConstant.FAILURE);
		responseDto.setStatusCode(MurphyConstant.CODE_FAILURE);
		// dto.getDto().setCreatedAt(dto.getDto().getCreatedAt() + "00:00:00");
		// dto.getDto().setUpdatedAt(dto.getDto().getUpdatedAt() + "00:00:00");
		try {
			if (!ServicesUtil.isEmpty(dto.getDto())) {

				String result = flareDowntimeCaptureDao.updateFlareDowntime(dto.getDto());
				responseDto.setStatus(MurphyConstant.SUCCESS);
				responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
				responseDto.setMessage("Flare Downtime " + MurphyConstant.UPDATE_SUCCESS);
				if (result.equals(MurphyConstant.SUCCESS)) {
					if (!ServicesUtil.isEmpty(dto.getIsProCountUpdate()) && dto.getIsProCountUpdate().equals(true)) {
						logger.error("[Murphy][FlareDowntimeCaptureFacade][PRO_COUNT][start updating PRO COUNT]");
						String mID = flareDowntimeCaptureDao.getMerrickId(dto.getDto());
						Date created_Date = flareDowntimeCaptureDao.getCreatedAt(dto.getDto());
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						format1.setTimeZone(TimeZone.getTimeZone("CST"));
						String dateStr = format1.format(created_Date);
						logger.error("[Murphy][FlareDowntimeCaptureFacade][PRO_COUNT][flareCaptureDTo][FlareVolume]"
								+ dto.getDto().getFlareVolume() + "[MerrickID]" + mID);
						FlareDowntimeCaptureLocal downtime = new FlareDowntimeCapture();
						FlareDowntimeCaptureDto procountDto = new FlareDowntimeCaptureDto();
						procountDto.setFlareCode(dto.getDto().getChildCode());
						procountDto.setFlareVolume(dto.getDto().getFlareVolume().floatValue());
						procountDto.setMerrickId(Integer.valueOf(mID));
						procountDto.setRecordDate(dateStr + " 00:00:00");
						logger.error("[Murphy][FlareDowntimeCaptureFacade][PRO_COUNT][MerrickId]"
								+ procountDto.getMerrickId() + "[Record Date]" + dto.getDto().getCreatedAt());
						com.murphy.integration.dto.ResponseMessage proCountResult = downtime
								.insertOrUpdateFlareDowntime(procountDto);
						if (proCountResult.getStatus().equalsIgnoreCase("true")) {
							responseDto.setMessage("Flare Downtime " + MurphyConstant.UPDATE_SUCCESS);
							responseDto.setStatus(MurphyConstant.SUCCESS);
							responseDto.setStatusCode(MurphyConstant.CODE_SUCCESS);
						} else {
							responseDto.setMessage("Pro Count Update Failed");
						}
					} else {
						responseDto.setMessage("Flare Downtime " + MurphyConstant.UPDATE_SUCCESS);
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
			logger.error("[Murphy][FlareDowntimeCaptureFacade][Exception Message]" + e);
		}
		return responseDto;
	}

}
