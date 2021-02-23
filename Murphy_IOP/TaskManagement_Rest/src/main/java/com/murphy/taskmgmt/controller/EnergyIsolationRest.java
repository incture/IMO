package com.murphy.taskmgmt.controller;

import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.murphy.taskmgmt.dto.ActivityLogDto;
import com.murphy.taskmgmt.dto.EIFormDto;
import com.murphy.taskmgmt.dto.EIFormListResponseDto;
import com.murphy.taskmgmt.dto.EnergyIsolationDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.EnergyIsolationFacadeLocal;
import com.murphy.taskmgmt.util.MurphyConstant;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/energyIsolation", produces = "application/json")
public class EnergyIsolationRest {
	
	@Autowired
	EnergyIsolationFacadeLocal facadeLocal;
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public ResponseMessage createEiForm(@RequestBody EIFormDto dto) {
		return facadeLocal.createForm(dto);
	}

	@RequestMapping(value = "/formLocation", method = RequestMethod.GET)
	public EIFormListResponseDto getFormsFromFacility(
			@RequestParam(value = "locationCode") String locationCode,
			@RequestParam(value = "locationType", required = false) String locationType,
			@RequestParam(value = "monthTime", required = false) String monthTime,
			@RequestParam(value = "weekTime") int weekTime,
			@RequestParam(value = "page") int page,
			@RequestParam(value = "page_size") int page_size,
			@RequestParam(value = "isActive", required = false) boolean isActive) {
		return facadeLocal.getFormsByLocation(locationCode, locationType, monthTime, weekTime, page, page_size,isActive);
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public EIFormDto getForm(@RequestParam(value = "formId") String formId) {
		return facadeLocal.getFormById(formId);
	}
	
	@RequestMapping(value = "/contractorList", method = RequestMethod.GET)
	 public List<String> getContractorList() {
		return facadeLocal.getContractorList();
	}
	
	@RequestMapping(value = "/shiftList", method = RequestMethod.GET)
	public List<String> getShiftList() {
		return facadeLocal.getShiftList();
	}
	
	@RequestMapping(value = "/reasonList", method = RequestMethod.GET)
	 public List<String> getReasonList() {
		return facadeLocal.getReasonList();
	}
	
	@RequestMapping(value = "/updateActivity", method = RequestMethod.GET)
	public ResponseMessage updateActivity(@RequestParam(value = "id") String id,
			@RequestParam(value = "value") String value) {
		return facadeLocal.updateActivityStatus(id, value);
	}
	
	@RequestMapping(value = "/createActivity", method = RequestMethod.POST)
	public ResponseMessage createActivity(@RequestBody ActivityLogDto dto) {
		return facadeLocal.createActivity(dto);
	}
	
	@RequestMapping(value = "/pushData", method = RequestMethod.GET)
	public void pushData() {
		facadeLocal.pushDataForNotification();
	}
	
	@RequestMapping(value = "/deleteLock", method = RequestMethod.GET)
	public String deleteLock(@RequestParam(value = "id") String id) {
		return facadeLocal.deleteLock(id);
	}
	
//	//Generate Pdf Report of EnergyIsolation
//	@RequestMapping(value = "/generatPdf", method = RequestMethod.POST)
//	public String generateEIPdfTemplate(@RequestBody EnergyIsolationDto dto) {
//		//JRXML File Loading
//		URL energyIsolationUrl = EnergyIsolationRest.class.getClassLoader().getResource(MurphyConstant.ENERGY_ISOLATION_TEMPLATE);
//		URL LogoURL = EnergyIsolationRest.class.getClassLoader().getResource(MurphyConstant.MURPHY_LOGO);
//		URL tickImageURL = EnergyIsolationRest.class.getClassLoader().getResource(MurphyConstant.CHECKBOXTICK);
//		return facadeLocal.generateEIPdfTemplate(dto,energyIsolationUrl.getPath(),LogoURL.getPath(),tickImageURL.getPath()).toString();
//	}
	
	//Trigger Mail for EI  Users
	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	public ResponseMessage sendEmail(@RequestBody EnergyIsolationDto dto){
		return facadeLocal.sendEmail(dto.getFormId(),dto.getAffectedPersonnelIdList());
	} 
}
