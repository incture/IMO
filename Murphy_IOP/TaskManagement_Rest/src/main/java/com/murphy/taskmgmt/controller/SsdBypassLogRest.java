/**
 * 
 */
package com.murphy.taskmgmt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dao.SsdBypassActivityLogDao;
import com.murphy.taskmgmt.dto.DeviceDto;
import com.murphy.taskmgmt.dto.DeviceListReponseDto;
import com.murphy.taskmgmt.dto.ReasonForBypassDto;
import com.murphy.taskmgmt.dto.ReasonForBypassResponseListDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.dto.RiskListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassActivityLogDto;
import com.murphy.taskmgmt.dto.SsdBypassListDto;
import com.murphy.taskmgmt.dto.SsdBypassLogListResponseDto;
import com.murphy.taskmgmt.dto.SsdBypassLogResponseDto;
import com.murphy.taskmgmt.service.interfaces.DeviceFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.ReasonForBypassFacadeLocal;
import com.murphy.taskmgmt.service.interfaces.SsdBypassLogFacadeLocal;

/**
 * @author Kamlesh.Choubey
 *
 */

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/bypassLog", produces = "application/json")
public class SsdBypassLogRest {

	@Autowired
	SsdBypassLogFacadeLocal facadeLocal;

	@Autowired
	DeviceFacadeLocal deviceFacade;

	@Autowired
	ReasonForBypassFacadeLocal reasonForBypassFacadeLocal;

	@Autowired
	SsdBypassActivityLogDao ssdBypassActivityLogDao;

//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public ArrayList<String> Test(@RequestParam(value = "locationCode") String locationCode) {
//		ArrayList<String> locCodes =  new ArrayList<>();
//		locCodes.add(locationCode);
//		return facadeLocal.getLocationHierarchy(locCodes);
//
//	}

	@RequestMapping(value = "/createBypassLog", method = RequestMethod.POST)
	public ResponseMessage createBypassLog(@RequestBody SsdBypassLogResponseDto dto) {

		return facadeLocal.createBypassLog(dto);
	}

	@RequestMapping(value = "/getBypassLogById", method = RequestMethod.GET)

	public SsdBypassLogResponseDto getBypassLogById(@RequestParam(value = "bypassId") String bypassId) {
		return facadeLocal.getBypassLogById(bypassId);
	}

	@RequestMapping(value = "/updateBypassLog", method = RequestMethod.PUT)
	public ResponseMessage updateBypassLog(@RequestBody SsdBypassLogResponseDto dto) {
		return facadeLocal.updateBypassLog(dto);
	}

	@RequestMapping(value = "/getBypassLogList", method = RequestMethod.GET)
	public SsdBypassLogListResponseDto getBypassLogList(@RequestParam(value = "locations") String locations,

			@RequestParam(value = "timePeriod") int timePeriod, @RequestParam(value = "pageNo") int pageNo,

			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "bypassLogStatus") String bypassLogStatus,
			@RequestParam(value = "locationType" , required = false) String locationType,
			@RequestParam(value = "isActive", required = false) boolean isActive) { // return
		// facadeLocal.getBypassLogList(location);
		return facadeLocal.getBypassLogList(locations, timePeriod, pageNo, pageSize, bypassLogStatus , locationType,isActive);
	} 

	@RequestMapping(value = "/createDeviceRecord", method = RequestMethod.POST)
	public ResponseMessage createDeviceRecord(@RequestBody DeviceDto dto) {
		return deviceFacade.createDeviceRecord(dto);
	}

	@RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
	public DeviceListReponseDto createDeviceRecord() {
		return deviceFacade.getDeviceList();
	}

	@RequestMapping(value = "/createReasonForBypass", method = RequestMethod.POST)
	public ResponseMessage createReasonForBypass(@RequestBody ReasonForBypassDto dto) {
		return reasonForBypassFacadeLocal.createReasonForBypass(dto);
	}

	@RequestMapping(value = "/getReasonForBypassList", method = RequestMethod.GET)
	public ReasonForBypassResponseListDto getReasonForBypassList() {
		return reasonForBypassFacadeLocal.getReasonForBypassList();
	}

	@RequestMapping(value = "/createBypassActivityLog", method = RequestMethod.POST)
	public ResponseMessage createBypassActivityLog(@RequestBody SsdBypassActivityLogDto dto) {
		return facadeLocal.createBypassActivityLog(dto);
	}

	// sendNotificationForEscalation and reminder
	@RequestMapping(value = "/sendNotificationForEscalation", method = RequestMethod.GET)
	public ResponseMessage sendNotificationForEscalation(@RequestParam(value = "hours") int hours,
			@RequestParam(value = "notificationTime") String notificationTime,
			@RequestParam(value = "zone") String zone) {
		return facadeLocal.sendNotificationForEscalation(hours, notificationTime,zone);
	}

	/*
	 * @RequestMapping(value = "/sendOutShiftNotificationForEscalation", method
	 * = RequestMethod.GET) public ResponseMessage
	 * sendOutShiftNotificationForEscalation(@RequestParam(value =
	 * "notificationTime") String notificationTime , @RequestParam(value =
	 * "hours") int hours) { return
	 * facadeLocal.outShiftNotification(notificationTime , hours); }
	 */

	// sendNotificationForShiftChange
	@RequestMapping(value = "/sendNotificationForShiftChange", method = RequestMethod.GET)
	public ResponseMessage sendNotificationForShiftChange(@RequestParam(value = "zone") String zone) {
		return facadeLocal.sendNotificationForShiftChange(zone);
	}

	// to update the operator's approval status after shift change
	@RequestMapping(value = "/updateOperatorRsponse", method = RequestMethod.GET)
	public ResponseMessage updateOperatorRsponse(@RequestParam(value = "activityLogId") String activityLogId,
			@RequestParam(value = "responseValue") boolean responseValue,
			@RequestParam(value = "operatorType") String operatorType,
			@RequestParam(value = "operatorId") String operatorId) {
		return facadeLocal.updateOperatorResponse(activityLogId, responseValue, operatorType, operatorId);
	}

	@RequestMapping(value = "/getBypassLogListByUserGroup", method = RequestMethod.GET)
	public List<SsdBypassListDto> getBypassLogListByUserGroup(
			@RequestParam(value = "technicalRole") String technicalRole,
			@RequestParam(value = "businessRole") String businessRole) {
		return facadeLocal.getBypassLogListByUserGroup(technicalRole, businessRole);

	}

	@RequestMapping(value = "/getUserLoginByPid", method = RequestMethod.GET)
	public HashMap<String, String> getUserLoginByPid(@RequestParam(value = "pid") String pid) {
		return facadeLocal.getUserLoginByPid(pid);
	}
	
	// This is for getting risk for bypass
	@RequestMapping(value = "/getRiskList", method = RequestMethod.GET)
	public RiskListResponseDto getRiskList() {
		return facadeLocal.getRiskLevelList();
	}

}
