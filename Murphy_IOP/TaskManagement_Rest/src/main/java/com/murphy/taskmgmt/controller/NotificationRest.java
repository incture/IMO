package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.integration.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.BypassNotificationDto;
import com.murphy.taskmgmt.dto.FracNotificationDto;
import com.murphy.taskmgmt.dto.NotificationDto;
import com.murphy.taskmgmt.service.NotificationFacade;
import com.murphy.taskmgmt.service.interfaces.EnergyIsolationFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.FracNotificationFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.NotificationFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.SsdBypassLogFacadeLocal;
import com.murphy.taskmgmt.websocket.FracServiceEndPoint;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/frachit", produces = "application/json")
public class NotificationRest {

	@Autowired
	FracNotificationFacadeLocal fracNotificationFacade;

	@Autowired
	NotificationFacadeLocal notificationFacadeLocal;

	@Autowired
	SsdBypassLogFacadeLocal facadeLocal;

	@Autowired
	EnergyIsolationFacadeLocal energyFacadeLocal;

	@RequestMapping(value = "/updateNotifyWell", method = RequestMethod.POST)
	public ResponseMessage updateFracNotification(@RequestBody List<FracNotificationDto> fracdto) {
		return fracNotificationFacade.updateFracNotification(fracdto);
	}

	@RequestMapping(value = "/notifyWell", method = RequestMethod.GET)
	public void fracNotification() {
		fracNotificationFacade.getFracHitDetails("swathi.vs@incture.com");
	}

	@RequestMapping(value = "/getNotification", method = RequestMethod.GET)
	public void getNotificationList() {
		notificationFacadeLocal.getNotificationList();
	}

	// @RequestMapping(value ="/updateHopperWells", method = RequestMethod.GET)
	// public void updateHopperWells(){
	// notificationFacadeLocal.updatePwHopper();
	// }

	@RequestMapping(value = "/updateBubbleAck", method = RequestMethod.POST)
	public ResponseMessage updateBubbleNotificationAlerts(@RequestBody List<NotificationDto> notifyDto) {
		return notificationFacadeLocal.updateBubbleAckDetails(notifyDto);
	}

	@RequestMapping(value = "/updateSafetyAppAckStatus", method = RequestMethod.POST)
	public ResponseMessage updateSafetyAppStatus(@RequestBody List<NotificationDto> bypassDtoList) {
		return notificationFacadeLocal.updateSafetyAppStatusDetails(bypassDtoList);
	}

	// sendNotificationForEscalation and reminder
	@RequestMapping(value = "/sendNotificationForEscalation", method = RequestMethod.GET)
	public com.murphy.taskmgmt.dto.ResponseMessage sendNotificationForEscalation(
			@RequestParam(value = "hours") int hours,
			@RequestParam(value = "notificationTime") String notificationTime,
			@RequestParam(value = "zone") String zone) {
		return facadeLocal.sendNotificationForEscalation(hours, notificationTime,zone);
	}

	// sendNotificationForShiftChange
	@RequestMapping(value = "/sendNotificationForShiftChange", method = RequestMethod.GET)
	public com.murphy.taskmgmt.dto.ResponseMessage sendNotificationForShiftChange(@RequestParam(value = "zone") String zone) {
		return facadeLocal.sendNotificationForShiftChange(zone);
	}

	// sendEnergyIsolation Notification
	@RequestMapping(value = "/pushData", method = RequestMethod.GET)
	public void test() {
		energyFacadeLocal.pushDataForNotification();
	}
	
	//send notification for future task
	@RequestMapping(value = "/sendNotificationForFutureTask", method = RequestMethod.GET)
	public ResponseMessage sendNotificationForFutureTask() {
		return notificationFacadeLocal.sendNotificationForFutureTask();
	}
}
