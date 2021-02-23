package com.murphy.taskmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.AckAlarmReqDto;
import com.murphy.taskmgmt.dto.CygnetAlarmFeedResponseDto;
import com.murphy.taskmgmt.dto.CygnetAlarmRequestDto;
import com.murphy.taskmgmt.dto.DowntimeCaptureHistoryDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
//import com.murphy.taskmgmt.scheduler.CygnetDataPullSchedular;
import com.murphy.taskmgmt.service.interfaces.AlarmFeedFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/alarmFeed", produces = "application/json")
public class AlarmFeedRest {

	@Autowired
	AlarmFeedFacadeLocal alarmFeedLocal;
	
//	@Autowired
//	CygnetDataPullSchedular cygnetDataPullSchedular;
	
//	@RequestMapping(value = "/getAlarmFeed", method = RequestMethod.GET)
//	public String getAlarmFeed(){
//		 cygnetDataPullSchedular.getAlarmFeed();
//		 return "success";
//	}
//	comment2
	
	@RequestMapping(value = "/getAlarm", method = RequestMethod.POST)
	public CygnetAlarmFeedResponseDto getAlarmFeed(@RequestBody CygnetAlarmRequestDto dto){
		return alarmFeedLocal.getAlarmList(dto);
	}
	
	@RequestMapping(value = "/updateAcknowledge", method = RequestMethod.POST)
	public ResponseMessage updateAlarmIsAcknowledge(@RequestBody AckAlarmReqDto requestDto){
		return alarmFeedLocal.updateAlarmIsAcknowledge(requestDto);
	}
	
	@RequestMapping(value = "/updateClassifier", method = RequestMethod.POST)
	public ResponseMessage updateAlarmDowntimeClassifier(@RequestBody DowntimeCaptureHistoryDto dto){
		return alarmFeedLocal.updateAlarmDowntimeClassifier(dto);
	}
	
}

