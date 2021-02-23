package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.AckAlarmReqDto;
import com.murphy.taskmgmt.dto.CygnetAlarmFeedResponseDto;
import com.murphy.taskmgmt.dto.CygnetAlarmRequestDto;
import com.murphy.taskmgmt.dto.DowntimeCaptureHistoryDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface AlarmFeedFacadeLocal {

	ResponseMessage updateAlarmDowntimeClassifier(DowntimeCaptureHistoryDto captureHistoryDto);

	CygnetAlarmFeedResponseDto getAlarmList(CygnetAlarmRequestDto alarmRequestDto);

	ResponseMessage updateAlarmIsAcknowledge(AckAlarmReqDto requestDto);

}
