package com.murphy.taskmgmt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.murphy.taskmgmt.dao.CygnetAlarmFeedDao;
import com.murphy.taskmgmt.dao.DowntimeCaptureHistoryDao;
import com.murphy.taskmgmt.dto.AckAlarmReqDto;
import com.murphy.taskmgmt.dto.CygnetAlarmFeedResponseDto;
import com.murphy.taskmgmt.dto.CygnetAlarmRequestDto;
import com.murphy.taskmgmt.dto.DowntimeCaptureHistoryDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.AlarmFeedFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Service("AlarmFeedFacade")
public class AlarmFeedFacade implements AlarmFeedFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(AlarmFeedFacade.class);


	public AlarmFeedFacade() {
	}

	@Autowired
	private CygnetAlarmFeedDao cygnetAlarmFeedDao;

	ResponseMessage message = null;

	@Autowired
	private DowntimeCaptureHistoryDao downtimeHistoryDao;

	@Override
	public ResponseMessage updateAlarmIsAcknowledge(AckAlarmReqDto requestDto) {
		message = new ResponseMessage();
		message.setMessage(MurphyConstant.ACKNOWLEDGED_FAILURE);
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(requestDto) && !ServicesUtil.isEmpty(requestDto.getPointIds())) {
			if (cygnetAlarmFeedDao.updateAlarmFeed(requestDto.getPointIds(), requestDto.getKey(), requestDto.getValue())
					.equals(MurphyConstant.SUCCESS)) {
				message.setMessage(MurphyConstant.ACKNOWLEDGED_SUCCESS);
				message.setStatus(MurphyConstant.SUCCESS);
				message.setStatusCode(MurphyConstant.CODE_SUCCESS);
			}
		} else
			message.setMessage(MurphyConstant.MAND_MISS);

		return message;
	}

	@Override
	public ResponseMessage updateAlarmDowntimeClassifier(DowntimeCaptureHistoryDto captureHistoryDto) {
		message = new ResponseMessage();
		message.setMessage(MurphyConstant.UPDATE_FAILURE);
		message.setStatus(MurphyConstant.FAILURE);
		message.setStatusCode(MurphyConstant.CODE_FAILURE);
		try{
		if (!ServicesUtil.isEmpty(captureHistoryDto.getLongDescription())
				&& !ServicesUtil.isEmpty(captureHistoryDto.getDownTimeClassifier()) && !ServicesUtil.isEmpty(captureHistoryDto.getLocationCode())) {
			String countryCode = ServicesUtil.getCountryCodeByLocation(captureHistoryDto.getLocationCode());

			if (MurphyConstant.EFS_CODE.equalsIgnoreCase(countryCode)) {
				message = downtimeHistoryDao.updateDowntimeHistory(captureHistoryDto);
			} else if (MurphyConstant.CA_CODE.equalsIgnoreCase(countryCode)) {
				message = downtimeHistoryDao.updateCanadaDowntimeHistory(captureHistoryDto);
			}
		} else
			message.setMessage(MurphyConstant.MAND_MISS);
		}
		catch(Exception e){
			logger.error("[Murphy][AlarmFeedFacade][updateAlarmDowntimeClassifier][Exception]"+e.getMessage());
		}
		return message;
	}

	@Override
	public CygnetAlarmFeedResponseDto getAlarmList(CygnetAlarmRequestDto alarmRequestDto) {
		String countryCode = null;
		String locationCode = null;
		try{
		if (!ServicesUtil.isEmpty(alarmRequestDto) && !ServicesUtil.isEmpty(alarmRequestDto.getLocations())) {
			if (alarmRequestDto.getLocations().contains(",")) {
				String[] locCodeArr = alarmRequestDto.getLocations().split(",");
				locationCode = locCodeArr[0].replace("'", "");
			} else {
				locationCode = alarmRequestDto.getLocations().replace("'", "");
			}
			countryCode = ServicesUtil.getCountryCodeByLocation(locationCode);

			if (MurphyConstant.EFS_CODE.equalsIgnoreCase(countryCode)) {
				return cygnetAlarmFeedDao.getAlarmFeeds(alarmRequestDto);
			} else if (MurphyConstant.CA_CODE.equalsIgnoreCase(countryCode)) {
				return cygnetAlarmFeedDao.getAlarmFeedsForCa(alarmRequestDto);
			}
		}
		}catch(Exception e){
			logger.error("[Murphy][AlarmFeedFacade][getAlarmList][Exception]"+e.getMessage());
		}
		return null;
	}

}
